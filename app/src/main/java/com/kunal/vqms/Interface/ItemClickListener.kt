package com.kunal.vqms.Interface

import android.view.View

/* @author kunal on 21/10/2020 */
/* Interface for item click in recycler view */

interface ItemClickListener {
    fun onClick(view: View, position:Int)
}