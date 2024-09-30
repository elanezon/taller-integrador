package com.example.ev_tec.ui.Carga

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ev_tec.databinding.FragmentCargaBinding

class CargaFragment : Fragment() {

    private var _binding: FragmentCargaBinding? = null
    private val binding get() = _binding!!

    private lateinit var timer: CountDownTimer
    private val twoHoursInMillis: Long = 2 * 60 * 60 * 1000 // 2 hours in milliseconds
    private val interval: Long = 1000 // 1 second interval

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCargaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        startTimer() // Start the timer when the fragment is created

        return root
    }

    private fun startTimer() {
        // Initialize the CountDownTimer
        timer = object : CountDownTimer(twoHoursInMillis, interval) {
            override fun onTick(millisUntilFinished: Long) {
                // Calculate the percentage of time passed and update the progress bar
                val progress = ((twoHoursInMillis - millisUntilFinished).toFloat() / twoHoursInMillis * 100).toInt()
                binding.progressBar.progress = progress
            }

            override fun onFinish() {
                // When the timer finishes, set the progress to 100%
                binding.progressBar.progress = 100
            }
        }

        // Start the timer
        timer.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Cancel the timer when the fragment's view is destroyed
        if (::timer.isInitialized) {
            timer.cancel()
        }
        _binding = null
    }
}
