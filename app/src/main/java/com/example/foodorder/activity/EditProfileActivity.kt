package com.example.foodorder.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.foodorder.databinding.ActivityEditProfileBinding
import com.example.foodorder.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var sessionManager: SessionManager
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        currentUser = sessionManager.getUser()

        currentUser?.let {
            setupUI(it)
        }

        binding.btnSave.setOnClickListener {
            saveProfile()
        }
    }

    private fun setupUI(user: User) {
        binding.edtName.setText(user.name)
        binding.edtPhone.setText(user.phone)
        binding.edtAvatarUrl.setText(user.avatarUrl)

        if (user.avatarUrl.isNotEmpty()) {
            Glide.with(this).load(user.avatarUrl).into(binding.imgAvatarPreview)
        }
    }

    private fun saveProfile() {
        val name = binding.edtName.text.toString().trim()
        val phone = binding.edtPhone.text.toString().trim()
        val avatarUrl = binding.edtAvatarUrl.text.toString().trim()

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }

        val uid = auth.currentUser?.uid ?: return

        val updatedUser = mapOf(
            "name" to name,
            "phone" to phone,
            "avatarUrl" to avatarUrl
        )

        db.collection("users").document(uid)
            .update(updatedUser)
            .addOnSuccessListener {
                // Cập nhật lại session
                val updated = User(uid, name, currentUser?.email ?: "", phone, avatarUrl)
                sessionManager.saveUser(updated)
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Cập nhật thất bại: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
