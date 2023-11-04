package ie.setu.domain

import org.joda.time.DateTime

data class Measurement (var id: Int,
                        var bodyPart:String,
                        var size: Double,
                        var measuredDate: DateTime,
                        var userId: Int)