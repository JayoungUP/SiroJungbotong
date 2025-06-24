package com.tukorea.sirojungbotong
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.yourapp.SignupExtraActivity

class TermActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.termsingup)

        // '가입 완료하기' 버튼 클릭 시 HomeActivity로 이동
        val completeBtn = findViewById<ImageView>(R.id.gotohome)
        completeBtn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        // '업장 등록하기' 버튼 클릭 시 MarketListupActivity로 이동
        val uploadBtn = findViewById<ImageView>(R.id.uploading)
        uploadBtn.setOnClickListener {
            val intent = Intent(this, SignupExtraActivity::class.java)
            startActivity(intent)
        }
    }
}