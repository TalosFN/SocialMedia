package com.example.socialmediaapp

import Post
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*

class CreatePostActivity : AppCompatActivity() {

    private lateinit var postContentEditText: EditText
    private lateinit var imageView: ImageView
    private lateinit var savePostButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    // Список изображений для выбора
    private val imageUris = listOf(
        R.drawable.vulkano, R.drawable.city, R.drawable.bluesky // укажите ваши картинки в drawable
    )
    private var selectedImageUri: Int? = null // URI выбранного изображения

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        postContentEditText = findViewById(R.id.etContent)
        imageView = findViewById(R.id.ivSelectedImage)
        savePostButton = findViewById(R.id.btnSubmit)

        // Показываем первое изображение для выбора
        imageView.setImageResource(imageUris[0])

        // Обработчик для выбора изображения
        imageView.setOnClickListener {
            showImagePicker()
        }

        savePostButton.setOnClickListener {
            val postContent = postContentEditText.text.toString()
            if (postContent.isNotEmpty() && selectedImageUri != null) {
                savePostToDatabase(postContent, selectedImageUri!!)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showImagePicker() {
        // Показываем диалог с выбором изображения
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Image")
        builder.setItems(imageUris.map { getString(it) }.toTypedArray()) { dialog, which ->
            selectedImageUri = imageUris[which]
            imageView.setImageResource(selectedImageUri!!)
        }
        builder.show()
    }

    private fun savePostToDatabase(content: String, imageResId: Int) {
        // Получаем uid текущего пользователя
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val post = Post(content, imageResId)
            val postRef = database.child("users").child(userId).child("posts").push()
            postRef.setValue(post)
                .addOnSuccessListener {
                    Toast.makeText(this, "Post created successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error creating post", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }
}

