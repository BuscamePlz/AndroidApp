package com.nighnight.puhrez.buscame

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_item_description.*
class ItemDescriptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_description)

        val marker = intent.extras.get(ITEM_MARKER) as ItemMarker
        itemName.apply {
            text = marker.name
        }

    }
}
