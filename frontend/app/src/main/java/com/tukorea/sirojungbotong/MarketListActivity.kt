package com.tukorea.sirojungbotong

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.david.siro.MarketUploadActivity
import com.tukorea.sirojungbotong.databinding.OnBoardBinding
import kotlin.jvm.java

class MarketListActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val bind= OnBoardBinding.inflate(layoutInflater)
        setContentView(bind.root)
        bind.OnboardSearch.setOnClickListener {
            try{
                val intent=Intent(this, MarketListActivity::class.java)
                startActivity(intent)
            }
            catch(e:Exception){
                Toast.makeText(this, "MarketListActivity를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        bind.Upload.setOnClickListener {
            try{
                val intent=Intent(this, MarketUploadActivity::class.java)
                startActivity(intent)
            }
            catch(e:Exception){
                Toast.makeText(this, "MarketListActivity를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
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


    }
}
}