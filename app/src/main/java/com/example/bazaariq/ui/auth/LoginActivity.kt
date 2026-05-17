package com.example.bazaariq.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bazaariq.databinding.ActivityLoginBinding
import com.example.bazaariq.ui.dashboard.DashboardActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSendOtp.setOnClickListener {
            val phone = binding.etPhone.text.toString()
            if (phone.length == 10) {
                // Show OTP field
                binding.otpLayout.visibility = View.VISIBLE
                Toast.makeText(
                    this,
                    "OTP Sent! Use 123456 for testing",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Enter valid 10 digit number!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.btnVerifyOtp.setOnClickListener {
            val otp = binding.etOtp.text.toString()
            // TEST MODE - accepts 123456
            if (otp == "123456" || otp.length == 6) {
                Toast.makeText(this, "Welcome to BazaarIQ! 🎉", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Enter OTP: 123456 (test mode)",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}