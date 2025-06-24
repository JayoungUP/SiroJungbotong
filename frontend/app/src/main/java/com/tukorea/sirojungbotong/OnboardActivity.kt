package com.tukorea.sirojungbotong

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tukorea.sirojungbotong.databinding.OnBoardBinding

class OnboardActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val bind= OnBoardBinding.inflate(layoutInflater)
        setContentView(bind.root)
        bind.OnboardSearch.setOnClickListener {
            try{
                val intent=Intent(this,HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            catch(e:Exception){
                Toast.makeText(this, "HomeActivity를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        bind.Upload.setOnClickListener {
            try {
                val intent = Intent(this, SignupKakaoActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "SignupKakaoActivity를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        bind.OnboardLogin.setOnClickListener {
            // 예시: LoginActivity 로 이동
            try {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "LoginActivity를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}