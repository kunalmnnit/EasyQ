package com.kunal.vqms.constants

import com.kunal.vqms.R

class Constants {
     companion object{
        val governmentOffices : List<Pair<Int,Pair<Double,Double>>> = listOf(Pair(R.string.office1,Pair(26.85082,75.81911)),
                                                                    Pair(R.string.office2,Pair(26.90495, 75.79088)),
                                                                    Pair(R.string.office3,Pair(26.9035, 75.79212)),
                                                                    Pair(R.string.office4,Pair(26.91515, 75.8005)),
                                                                    Pair(R.string.office5,Pair(26.91844, 75.79467)),
                                                                    Pair(R.string.office6,Pair(26.90275, 75.80131)),
                                                                    Pair(R.string.office7,Pair(26.9086, 75.78249)))
         val rationShops : List<Pair<Int,Pair<Double,Double>>> = listOf(Pair(R.string.shop1,Pair(26.87593, 75.72452)),
                                                                     Pair(R.string.shop2,Pair(26.80503, 75.82091)),
                                                                     Pair(R.string.shop3,Pair(26.86305, 75.75669)),
                                                                     Pair(R.string.shop4,Pair(26.88102, 75.79288)))
     }
}