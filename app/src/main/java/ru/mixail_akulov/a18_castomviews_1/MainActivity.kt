package ru.mixail_akulov.a18_castomviews_1

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import ru.mixail_akulov.a18_castomviews_1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val token = Any()
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().add(R.id.includeButtons, ButtonsTopFragment()).commit()

        /**
        8.
       Назначаем слушателей нажатиям кнопок и определяем действия при нажатиях, для чего надо в классе
        BottomButtonsView прописать методы для данных действий.
         */
        binding.atrrsXmlButtons.setListener {
            if (it == BottomButtonAction.POSITIVE) {

                binding.atrrsXmlButtons.isProgressMode = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    handler.postDelayed({
                        binding.atrrsXmlButtons.isProgressMode = false
                        binding.atrrsXmlButtons.setPositiveButtonText("Updated OK")
                        Toast.makeText(this, "Positive Button Pressed", Toast.LENGTH_SHORT).show()
                    }, token, 2000)
                }

                binding.atrrsXmlButtons.setPositiveButtonText("Updated OK")
                Toast.makeText(this, "Positive atrrs.xml Button Pressed", Toast.LENGTH_SHORT).show()

            } else if (it == BottomButtonAction.NEGATIVE) {
                binding.atrrsXmlButtons.setNegativeButtonText("Updated Cancel")
                Toast.makeText(this, "Negative atrrs.xml Button Pressed", Toast.LENGTH_SHORT).show()
            }
        }

        /**
        8.
        Назначаем слушателей нажатиям кнопок и определяем действия при нажатиях
         */
        binding.styleButtons.setListener {
            if (it == BottomButtonAction.POSITIVE) {

                binding.styleButtons.setPositiveButtonText("Updated OK")
                Toast.makeText(this, "Positive Style Button Pressed", Toast.LENGTH_SHORT).show()

            } else if (it == BottomButtonAction.NEGATIVE) {
                binding.styleButtons.setNegativeButtonText("Updated Cancel")
                Toast.makeText(this, "Negative Style Button Pressed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(token)
    }

}