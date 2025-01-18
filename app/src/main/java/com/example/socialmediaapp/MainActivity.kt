package com.example.socialmediaapp

import Post
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var btn: Button
    private val postsList = mutableListOf<Post>()
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var firestore: FirebaseFirestore
    private lateinit var tvUsername: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        tvUsername = findViewById(R.id.tvUsername)

        loadNickname()

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        btn = findViewById(R.id.btnCreatePost)
        recyclerView = findViewById(R.id.rvPosts)
        recyclerView.layoutManager = LinearLayoutManager(this)
        postAdapter = PostAdapter(postsList)
        recyclerView.adapter = postAdapter
        val viewOtherPostsButton = findViewById<Button>(R.id.viewOtherPostsButton)

        loadUserPosts()
        btn.setOnClickListener{
            val intent = Intent(this, CreatePostActivity::class.java)
            startActivity(intent)
        }
        viewOtherPostsButton.setOnClickListener {
            // Переходим на экран с постами других пользователей
            val intent = Intent(this, OtherUsersPostsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadUserPosts() {
        // Получаем uid текущего пользователя
       val userId = auth.currentUser?.uid
        if (userId != null) {
            val postsRef = database.child("users").child(userId).child("posts")
            postsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    postsList.clear()
                    for (postSnapshot in snapshot.children) {
                        val post = postSnapshot.getValue(Post::class.java)
                        if (post != null) {
                            postsList.add(post)
                        }
                    }
                    postAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MainActivity, "Error loading posts", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }
    private fun loadNickname() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    val nickname = document.getString("nickname")
                    val nicknameTextView = findViewById<TextView>(R.id.tvUsername)
                    nicknameTextView.text = nickname ?: "Unknown User"
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to load nickname", Toast.LENGTH_SHORT).show()
                }
        }
    }
}







