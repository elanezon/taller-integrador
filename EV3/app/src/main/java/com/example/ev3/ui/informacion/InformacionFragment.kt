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
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.ValueFormatter
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.Date

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

            lineChart.setDragEnabled(true) // Habilitar desplazamiento (scroll)
            lineChart.setScaleEnabled(true) // Habilitar zoom para ampliar la vista

            // Configuración de los ejes
            val xAxis = lineChart.xAxis
            xAxis.textColor = Color.WHITE  // Cambiar el color del eje X a blanco
            xAxis.setDrawAxisLine(true)  // Activar la línea del eje X
            xAxis.setDrawGridLines(false)  // Desactivar las líneas de la cuadrícula en el eje X
            xAxis.setDrawLabels(true)  // Mostrar las etiquetas del eje X

            xAxis.granularity = 1f
            //xAxis.setLabelCount(5, true) // Limitar el número de etiquetas
            xAxis.setLabelRotationAngle(-45f)  // Rotar las etiquetas

            // Asegurarse de que el eje X está habilitado y visible en la parte inferior
            xAxis.isEnabled = true // Aseguramos que el eje X está habilitado

            // Configuración del eje izquierdo (Y)
            val yAxisLeft = lineChart.axisLeft
            yAxisLeft.textColor = Color.WHITE  // Cambiar el color del eje Y a blanco
            yAxisLeft.setDrawGridLines(true)  // Activar las líneas de la cuadrícula en el eje Y

            // Agregar "gramos" a las etiquetas del eje Y
            yAxisLeft.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "${value.toInt()} g"  // Mostrar los valores con el sufijo "g"
                }
            }

            // Desactivar el eje derecho (Y)
            lineChart.axisRight.isEnabled = false  // Desactivar el eje derecho

            // Configurar el eje X para mostrar fechas en lugar de números
            xAxis.valueFormatter = object : ValueFormatter() {
                private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                override fun getFormattedValue(value: Float): String {
                    // Convertir el valor flotante a un timestamp en Long
                    val timestamp = value.toLong()
                    return dateFormat.format(Date(timestamp))
                }
            }

            // Asegurarse de que las etiquetas del eje X no se sobrepongan
            xAxis.position = XAxis.XAxisPosition.BOTTOM // Colocar las etiquetas en la parte inferior



            // Crear un LineDataSet con los valores obtenidos
            val dataSet = LineDataSet(entries, "CO₂ Ahorrado")
            dataSet.color = Color.CYAN
            dataSet.valueTextColor = Color.WHITE

            // Configuración de la leyenda
            val legend = lineChart.legend
            legend.isEnabled = true  // Habilitar la leyenda
            legend.textColor = Color.WHITE  // Cambiar el color del texto de la leyenda a blanco
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT // Alineación horizontal a la izquierda
            legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP // Alineación vertical en la parte superior
            legend.orientation = Legend.LegendOrientation.HORIZONTAL // La leyenda será horizontal
            legend.setDrawInside(true)  // Asegurar que la leyenda esté dentro del área del gráfico

            // Configurar la posición de la leyenda
            legend.xEntrySpace = 10f // Espacio entre las entradas de la leyenda
            legend.yEntrySpace = 5f  // Espacio entre las entradas de la leyenda

            // Crear LineData y asignarlo al gráfico
            val lineData = LineData(dataSet)
            lineChart.data = lineData

            // Configurar el título del gráfico
            lineChart.description.isEnabled = false  // Desactivar la descripción predeterminada
            lineChart.setTouchEnabled(true)  // Habilitar la interacción táctil

            // Actualizar el gráfico
            lineChart.invalidate() // Refrescar el gráfico para mostrar los nuevos datos
        })

        return binding.root
    }
}