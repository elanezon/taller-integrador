package com.example.ev3.ui.informacion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ev3.databinding.FragmentInformacionBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import androidx.lifecycle.Observer
import android.graphics.Color

class InformacionFragment : Fragment() {

    private lateinit var binding: FragmentInformacionBinding
    private lateinit var informacionViewModel: InformacionViewModel
    private lateinit var lineChart: LineChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInformacionBinding.inflate(inflater, container, false)
        lineChart = binding.lineChart

        // Obtener una instancia del ViewModel
        informacionViewModel = ViewModelProvider(this).get(InformacionViewModel::class.java)

        // Observar los datos de CO₂ ahorrado
        informacionViewModel.ahorrosCO2.observe(viewLifecycleOwner, Observer { entries ->
            // Crear un LineDataSet con los valores obtenidos
            val dataSet = LineDataSet(entries, "CO₂ Ahorrado")
            dataSet.color = Color.GREEN
            dataSet.valueTextColor = Color.BLACK
            // Crear LineData y asignarlo al gráfico
            val lineData = LineData(dataSet)
            lineChart.data = lineData

            // Actualizar el gráfico
            lineChart.invalidate() // Refrescar el gráfico para mostrar los nuevos datos
        })

        return binding.root
    }
}