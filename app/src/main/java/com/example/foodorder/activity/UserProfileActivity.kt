package com.example.foodorder.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.foodorder.databinding.ActivityUserProfileBinding
import com.example.foodorder.model.User
import com.example.foodorder.activity.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var sessionManager: SessionManager
    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        currentUser = sessionManager.getUser()

        if (currentUser != null) {
            setupUI()
        } else {
            fetchUserFromFirebase()
        }

        setupActions()
    }

    private fun fetchUserFromFirebase() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            goToLogin()
            return
        }

        FirebaseFirestore.getInstance().collection("users").document(uid)
            .get()
            .addOnSuccessListener { doc ->
                val name = doc.getString("name") ?: ""
                val email = FirebaseAuth.getInstance().currentUser?.email ?: ""
                val phone = doc.getString("phone") ?: ""
                val avatar = doc.getString("avatarUrl") ?: ""

                currentUser = User(uid, name, email, phone, avatar)
                sessionManager.saveUser(currentUser!!)
                setupUI()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Lỗi tải thông tin", Toast.LENGTH_SHORT).show()
                goToLogin()
            }
    }

    private fun setupUI() {
        binding.tvName.text = currentUser?.name
        binding.tvEmail.text = currentUser?.email
        binding.tvPhone.text = currentUser?.phone

        currentUser?.avatarUrl?.let { url ->
            if (url.isNotEmpty()) {
                Glide.with(this).load(url).into(binding.imgAvatar)
            }
        }
    }

    private fun setupActions() {
        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            sessionManager.clearSession()
            goToLogin()
        }
    }

    private fun goToLogin() {
        startActivity(Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }
    override fun onResume() {
        super.onResume()
        // Reload dữ liệu nếu user đã cập nhật profile
        currentUser = sessionManager.getUser()
        if (currentUser != null) {
            setupUI()
        }
    }

}
