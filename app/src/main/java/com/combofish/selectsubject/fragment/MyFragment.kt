package com.combofish.selectsubject.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.combofish.selectsubject.R

class MyFragment : Fragment() {
    var message: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_base, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewById = view.findViewById<TextView>(R.id.textView)
        viewById.text = message
    }
}