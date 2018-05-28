package com.example.circledemo

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.example.circledemo.OnThreeClick.Companion.PAI

class MainActivity : OnThreeClick, AppCompatActivity() {

    override fun onThreeClick(a: Int): Int = PAI + 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = layoutInflater.cloneInContext(this)
        inflater.factory2 = object : LayoutInflater.Factory2 {
            override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
                return if (name == "mytagssss") {
                    CircleView(context, attrs)
                } else null
            }

            override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
                return null
            }
        }
        val view = inflater.inflate(R.layout.activity_main, null)
        setContentView(view)
    }

}

interface OnThreeClick {
    open fun onThreeClick(a: Int): Int

    companion object{
        val PAI = 3
    }
}

open class A() {
    constructor(a:Int):this()

    constructor(a:Int, b:Int):this(a)

    fun lock(str: String, body: () -> Int): Int {
        str.toString()
        return body()
    }

    companion object PaiClass{
        val PAI = 3.14
    }
}

class B : A(0,0) {

    fun lockA(str: String, body: () -> Int): Int {
        str.toString()
        PAI>3
        return body()
    }
}
