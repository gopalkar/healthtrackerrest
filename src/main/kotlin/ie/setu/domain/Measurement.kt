package ie.setu.domain


import org.joda.time.DateTime

data class Measurement (var id: Int,
                        var weight:Double,
                        var chest: Double,
                        var bicep: Double,
                        var neck: Double,
                        var abdomen: Double,
                        var waist: Double,
                        var lowerWaist: Double,
                        var thigh: Double,
                        var cough: Double,
                        var measuredDate: DateTime,
                        var userId: Int)