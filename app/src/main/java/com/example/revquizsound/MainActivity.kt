package com.example.revquizsound

import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //SoundPool型のインスタンス変数のフィードプロパティを宣言
    private lateinit var soundPool: SoundPool;
    //効果音の音源(Sound)のリソース
    private var soundResId = 0;//仮の番号で初期化

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.quizbutton.setOnClickListener {
            val intent = Intent(this, QuestActivity::class.java)
            startActivity(intent)
        }

        this.Anserbutton.setOnClickListener{
            soundPool.play(soundResId, 1.0f, 1.0f, 0, 0, 1.0f)
        }

        //SoundPoolクラスのインスタンスを生成して変数に代入
        this.soundPool =
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                //LOLLIPOPより古い端末の時
                SoundPool(
                    2,  //同時に鳴らせる音源数の設定
                    AudioManager.STREAM_ALARM,//オーディオの種類
                    0)//音源品質
            }else{
                //新しい端末の時
                //オーディオ設定
                val audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM).build();
                //オーディオ設定を使ってSoundPoolのインスタンス
                SoundPool.Builder().setMaxStreams(1)//同時音源数(1)
                    .setAudioAttributes(audioAttributes).build()//オーディオ設定を登録
            }
    }
        override fun onResume() {
            super.onResume()
            SoundPool(
                2,  //同時に鳴らせる音源数の設定
                AudioManager.STREAM_ALARM,//オーディオの種類
                0)//音源品質
            //鳴らすサウンドファイルのリソースID
            this.soundResId = soundPool.load(
                this,//アクティビティのインスタンス
                R.raw.anssound,//鳴らす音源のリソース
                1//音の優先順位
            )
        }
        //画面が隠れるときに呼ばれるイベントコールバックメソッド
        override fun onPause() {
            super.onPause()
            //サウンドプールインスタンスをメモリから解放
            this.soundPool.release()
        }
}
