package com.nighnight.puhrez.buscame

import android.location.Location
import android.os.Parcelable
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import java.io.Serializable
import java.net.URL
import java.util.*

data class ItemMarker(val name: String, val id: UUID,
                      val long: Double, val lat: Double) : Serializable {
    fun latLng(): LatLng = LatLng(this.lat, this.long)
    companion object {
        fun generateFromLocation(loc: Location): ItemMarker {
            var rand = Random()
            var name = "RandomItem${rand.nextInt()}"
            return ItemMarker("RandomItem${rand.nextInt()}",
                    UUID.randomUUID(),
                    varyDouble(loc.longitude, rand, .004),
                    varyDouble(loc.latitude, rand, .004))
        }
    }
}

data class Item(val name: String, val id: UUID,
                val photoUrls: Array<URL>, val long: Double,
                val lat: Double, val description: String) {
    var latLng = LatLng(lat, long)
    companion object {
        fun generateFromItemMarker(marker: ItemMarker): Item {
            return Item(marker.name, marker.id,
                    arrayOf(URL("https://dognkittycity.org/images/com_petlist/full/s271a3936398m9671467.jpg")),
                    marker.long, marker.lat,
                    """Janice along with apparent siblings Jane and Janet were left at the shelter gate.
These sweet girls were a little frightened at being dumped, but have turned out to be sweet, affectionate teenagers""")
        }
    }
}