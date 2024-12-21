package com.ahmedabdullah.metro

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var fromStation: Spinner
    lateinit var toStation: Spinner
    lateinit var numOfStations: TextView
    lateinit var ticketPrice: TextView
    lateinit var directionText: TextView
    lateinit var bestRoute: TextView
    lateinit var time: TextView

    val line1 = listOf("New El Marg",
        "El Marg",
        "Ezbet El Nakhl",
        "Ain Shams",
        "El Matareyya",
        "Helmeyet El Zaitoun",
        "Hadayeq El Zaitoun",
        "Saray El Qobba",
        "Hammamat El Qobba",
        "Kobri El Qobba",
        "Manshiet El Sadr",
        "El Demerdash",
        "Ghamra",
        "El Shohadaa",
        "Orabi",
        "Nasser",
        "Sadat",
        "Saad Zaghloul",
        "El Sayeda Zeinab",
        "El Malek El Saleh",
        "Mar Girgis",
        "El Zahraa",
        "Dar El Salam",
        "Hadayeq El Maadi",
        "Maadi",
        "Sakanat El Maadi",
        "Tora El Balad",
        "Kozzika",
        "Tora El Asmant",
        "El Maasara",
        "Hadayeq Helwan",
        "Wadi Hof",
        "Helwan University",
        "Ain Helwan",
        "Helwan"
    )
    val line2 = listOf("El Moneeb",
        "Sakiat Mekki",
        "Omm El Misryeen",
        "El Giza",
        "Faisal",
        "Cairo University",
        "Bohooth",
        "Dokki",
        "Opera",
        "Sadat",
        "Mohamed Naguib",
        "Attaba",
        "El Shohadaa",
        "Massara",
        "Rod El Farag",
        "St. Teresa",
        "Khalafawy",
        "Mezallat",
        "Koliet El Zeraa",
        "Shubra El Kheima"
    )
    val line3 = listOf("Adly Mansour",
        "El Haykestep",
        "Omar Ibn El Khattab",
        "Qobaa",
        "Hesham Barakat",
        "El Nozha",
        "Nadi El Shams",
        "Alf Maskan",
        "Heliopolis",
        "Haroun",
        "Al Ahram",
        "Koleyet El Banat",
        "Stadium",
        "Fair Zone",
        "Abbassia",
        "Abdou Pasha",
        "El Geish",
        "Bab El Shaaria",
        "Attaba",
        "Nasser",
        "Maspero",
        "Kit Kat"
    )
    val interSections = listOf("El Shohadaa", "Attaba", "Sadat", "Nasser")
    val allLines = listOf(line1 + line2 + line3).flatten()
    var timeOfTrip = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        fromStation = findViewById(R.id.fromSpinner)
        toStation = findViewById(R.id.toSpinner)
        numOfStations = findViewById(R.id.numOfStation)
        ticketPrice = findViewById(R.id.ticketPrice)
        directionText = findViewById(R.id.direction)
        bestRoute = findViewById(R.id.stationsText)
        time = findViewById(R.id.timeOfTrip)

        val linesAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, allLines)
        fromStation.adapter = linesAdapter
        toStation.adapter = linesAdapter
    }

    fun show(view: View) {

        val startStation = fromStation.selectedItem.toString()
        val endStation = toStation.selectedItem.toString()

            val (startLine, endLine) = findDifferentLines(line1 , line2, line3, startStation, endStation)

            var shortestRoute = listOf<String>()
            var minStations = 0
            var firstDirection = ""
            var lastDirection = ""


            val oneLine = getSameLineOfStations(line1 , line2, line3, startStation, endStation)
            if (startLine == oneLine && endLine == oneLine){
                getStationsOneLine(oneLine, startStation, endStation)
            } else {
                val commonIntersections = interSections.filter { startLine.contains(it) && endLine.contains(it) }

                commonIntersections.forEach { intersection ->
                    val startStationIndex = startLine.indexOf(startStation)
                    val startIntersectionIndex = startLine.indexOf(intersection)
                    val endStationIndex = endLine.indexOf(endStation)
                    val endIntersectionIndex = endLine.indexOf(intersection)
                    if (startLine.contains(intersection) && endLine.contains(intersection)) {
                        val startToIntersection =
                            kotlin.math.abs(startLine.indexOf(startStation) - startLine.indexOf(intersection))
                        val intersectionToEnd =
                            kotlin.math.abs(endLine.indexOf(intersection) - endStationIndex)
                        minStations = startToIntersection + intersectionToEnd + 1

                        shortestRoute = if (
                            startStationIndex < startIntersectionIndex && endStationIndex > endIntersectionIndex
                        ) {
                            startLine.subList(
                                minOf(
                                    startStationIndex,
                                    startIntersectionIndex
                                ),
                                maxOf(startStationIndex, startIntersectionIndex) + 1
                            ) +
                                    endLine.subList(
                                        minOf(
                                            endIntersectionIndex,
                                            endStationIndex
                                        ),
                                        maxOf(
                                            endIntersectionIndex,
                                            endStationIndex
                                        ) + 1
                                    ).drop(1)
                        } else if (startStationIndex < startIntersectionIndex && endStationIndex < endIntersectionIndex){
                            startLine.subList(
                                minOf(
                                    startStationIndex,
                                    startIntersectionIndex
                                ),
                                maxOf(startStationIndex, startIntersectionIndex) + 1
                            ) +
                                    endLine.subList(
                                        minOf(
                                            endIntersectionIndex,
                                            endStationIndex
                                        ),
                                        maxOf(
                                            endIntersectionIndex,
                                            endStationIndex
                                        ) + 1
                                    ).reversed().drop(1)
                        }
                        else if (startStationIndex > startIntersectionIndex && endStationIndex > endIntersectionIndex
                        ) {
                            startLine.subList(
                                minOf(
                                    startStationIndex,
                                    startIntersectionIndex
                                ),
                                maxOf(startStationIndex, startIntersectionIndex) + 1
                            ).reversed() +
                                    endLine.subList(
                                        minOf(
                                            endIntersectionIndex,
                                            endStationIndex
                                        ),
                                        maxOf(
                                            endIntersectionIndex,
                                            endStationIndex
                                        ) + 1
                                    ).drop(1)
                        } else if (startStationIndex > startIntersectionIndex && endStationIndex < endIntersectionIndex
                        ) {
                            startLine.subList(
                                minOf(
                                    startStationIndex,
                                    startIntersectionIndex
                                ),
                                maxOf(startStationIndex, startIntersectionIndex) + 1
                            ).reversed() +
                                    endLine.subList(
                                        minOf(
                                            endIntersectionIndex,
                                            endStationIndex
                                        ),
                                        maxOf(
                                            endIntersectionIndex,
                                            endStationIndex
                                        ) + 1
                                    ).reversed().drop(1)
                        } else {
                            shortestRoute
                        }
                    }

                    if (startStationIndex < startIntersectionIndex){
                        firstDirection = "${startLine.last()} Direction"
                    } else {
                        firstDirection = "${startLine.first()} Direction"
                    }

                    if (endIntersectionIndex > endStationIndex){
                        lastDirection = "${endLine.first()} Direction"
                    }else{
                        lastDirection = "${endLine.first()} Direction"
                    }
                }

                timeOfTrip = minStations * 2

                numOfStations.text = "Stations: $minStations"
                priceOfTicket(minStations)
                time.text = "$timeOfTrip Mins"
                directionText.text = "First Direction: $firstDirection, Change to: $lastDirection"
                bestRoute.text = "${shortestRoute.joinToString("  ->  ")}"
//                println("عدد المحطات: $minStations")
//                println(shortestRoute.joinToString(" -> "))
//                println("First Direction: $firstDirection, Change to: $lastDirection")
            }


    }
    fun priceOfTicket (numOfStations: Int){
        val ticket = when{
            numOfStations <= 9 -> 8
            numOfStations <= 16 -> 10
            numOfStations <= 23 -> 15
            else -> 20
        }

        ticketPrice.text = "Ticket Price: $ticket EGP"
    }

    fun findDifferentLines (line1 :List<String>, line2: List<String>, line3: List<String>, startStation: String, endStation: String): Pair<List<String>, List<String>>{
        val startLine = when{
            line1.contains(startStation) -> line1
            line2.contains(startStation) -> line2
            else -> line3
        }
        val endLine = when{
            line1.contains(endStation) -> line1
            line2.contains(endStation) -> line2
            else -> line3
        }
        return  Pair(startLine, endLine)
    }

    fun getSameLineOfStations(line1 :List<String>, line2: List<String>, line3: List<String>, startStation: String, endStation: String): List<String>{
        val sameLine = when{
            line1.contains(startStation) && line1.contains(endStation) -> line1
            line2.contains(startStation) && line2.contains(endStation) -> line2
            line3.contains(startStation) && line3.contains(endStation) -> line3
            else -> emptyList()
        }
        return sameLine
    }

    fun getStationsOneLine(line : List<String>, startStation: String, endStation: String): Pair<Int, List<String>>{

        var numOfStation = 0
        val stations: List<String>
        var direction = "";

        direction = if (line.indexOf(endStation) > line.indexOf(startStation)) {
            "${line.last()} Direction"
        } else {
            "${line.first()} Direction"
        }
        val startIndex = line.indexOf(startStation)
        val endIndex = line.indexOf(endStation)
        numOfStation = kotlin.math.abs(endIndex - startIndex)
        stations = line.subList(minOf(startIndex, endIndex), maxOf(startIndex, endIndex ))



        numOfStations.text = "Stations: $numOfStation"
        priceOfTicket(numOfStation)
        time.text = "$timeOfTrip Mins"
        directionText.text = "Direction: $direction"
        if (startIndex > endIndex) {
            bestRoute.text = stations.reversed().joinToString("  ->  ")
        } else {
            bestRoute.text = stations.joinToString("  ->  ")
        }


//            println("عدد المحطات: $numOfStation")
//            println(direction);

        return  Pair(numOfStation, stations)

    }

}