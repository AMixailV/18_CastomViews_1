package ru.mixail_akulov.a18_castomviews_1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mixail_akulov.a18_castomviews_1.databinding.FragmentButtonsTopBinding

class ButtonsTopFragment : Fragment() {

    private lateinit var binding: FragmentButtonsTopBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentButtonsTopBinding.inflate(inflater, container, false)

        with (binding.includeTopButtons) {
            positiveButton.text = getString(R.string.ok)
            negativeButton.text = getString(R.string.cancel)

            positiveButton.setOnClickListener {
                progress.visibility = View.VISIBLE
                positiveButton.visibility = View.INVISIBLE
                negativeButton.visibility = View.VISIBLE
            }

            negativeButton.setOnClickListener {
                progress.visibility = View.GONE
                positiveButton.visibility = View.VISIBLE
                negativeButton.visibility = View.INVISIBLE
            }
        }

        return binding.root
    }
}