package com.geohunt.data.repository.game

import com.geohunt.domain.repository.LoadingGameRepository
import javax.inject.Inject

class LoadingGameRepositoryImpl @Inject constructor(): LoadingGameRepository {
    override fun getLoadingMessage(): List<String> {
        return listOf(
            "Preparing your world map…",
            "Spinning the globe…",
            "Generating new location…",
            "Planting the pins…",
            "Calculating your coordinates…",
            "Hiding landmarks…",
            "Loading street view…",
            "Blurring the borders…",
            "Adjusting your compass…",
            "Checking the terrain…",
            "Placing roads and rivers…",
            "Zooming into the area…",
            "Hiding the clues…",
            "Randomizing your starting point…",
            "Checking satellite data…",
            "Aligning the map tiles…",
            "Spawning your challenge…",
            "Loading map textures…",
            "Setting up the location hints…",
            "Analyzing nearby cities…",
            "Preparing your travel adventure…",
            "Placing mountains and lakes…",
            "Shuffling the world’s locations…",
            "Spinning up the map engine…",
            "Generating scenic views…",
            "Loading points of interest…",
            "Adjusting street angles…",
            "Calibrating the zoom level…",
            "Preparing your virtual tour…",
            "Rendering the roads…",
            "Hiding signs and markers…",
            "Aligning the landmarks…",
            "Preparing your mystery spot…",
            "Mixing urban and rural areas…",
            "Shuffling coordinates…",
            "Placing your destination…",
            "Checking map accuracy…",
            "Loading your global adventure…",
            "Preparing hints and clues…",
            "Rotating the camera…",
            "Loading buildings and houses…",
            "Setting up the background scenery…",
            "Adjusting lighting and shadows…",
            "Preparing your challenge map…",
            "Calculating random spawn points…",
            "Aligning rivers and lakes…",
            "Zooming to your challenge spot…",
            "Spawning hidden streets…",
            "Loading textures for realism…",
            "Preparing your world discovery…",
            "Randomizing your location challenge…"
        )

    }

    override fun getTipsMessage(): List<String> {
        return listOf(
            "Observe the road markings carefully.",
            "Check which side of the road cars are driving on.",
            "Look for language on signs or billboards.",
            "Pay attention to the architecture.",
            "Notice the types of vehicles around you.",
            "Watch the vegetation and climate.",
            "Look at what people are wearing.",
            "License plates can reveal a lot.",
            "Flags or national symbols are big clues.",
            "Traffic signs often have regional styles.",

            "Big cities often have unique public transport.",
            "Store names might reveal the country.",
            "Look for famous skylines or tall buildings.",
            "Street lights and bus stops can be clues.",
            "Company logos may indicate the region.",

            "Trees and soil color can hint the area.",
            "Mountains can help narrow it down.",
            "Farms and fences differ by country.",
            "Is the road paved or dirt?",

            "Driving on the left? Think UK, Japan, Thailand.",
            "Driving on the right? Think Europe or the Americas.",

            "The sun position can help you find direction.",
            "Utility poles are surprisingly useful.",
            "Road edge line colors vary by country.",
            "Website domains on signs are powerful hints.",

            "Trust your instincts.",
            "Wild guesses sometimes win.",
            "Don’t stay in one spot too long.",
            "Small details make big differences.",
            "Zoom in and explore."
        )
    }
}