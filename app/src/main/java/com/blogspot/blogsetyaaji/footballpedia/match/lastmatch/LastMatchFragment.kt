package com.blogspot.blogsetyaaji.footballpedia.match.lastmatch


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.blogspot.blogsetyaaji.footballmatchschedule.util.invisible
import com.blogspot.blogsetyaaji.footballmatchschedule.util.visible
import com.blogspot.blogsetyaaji.footballpedia.R
import com.blogspot.blogsetyaaji.footballpedia.adapter.LastMatchAdapter
import com.blogspot.blogsetyaaji.footballpedia.api.ApiRepository
import com.blogspot.blogsetyaaji.footballpedia.match.detailmatch.DetailMatchActivity
import com.blogspot.blogsetyaaji.footballpedia.model.match.EventsItem
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_last_match.*
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivity

class LastMatchFragment : Fragment(), LastMatchView {

    private val events: MutableList<EventsItem> = mutableListOf()
    private lateinit var adapterLast: LastMatchAdapter
    private lateinit var lastMatchPresenter: LastMatchPresenter
    private lateinit var league: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_last_match, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        league = spinnerMatch.selectedItem.toString()
        league = "4328"

        spinnerMatch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    0 -> league = "4328"
                    1 -> league = "4329"
                    2 -> league = "4331"
                    3 -> league = "4332"
                    4 -> league = "4334"
                    5 -> league = "4335"
                }

                lastMatchPresenter.getPrevMatchList(league)
            }

        }

        matchswipe.setColorSchemeColors(
            Color.MAGENTA,
            Color.GREEN,
            Color.YELLOW,
            Color.RED
        )

        lv_match.layoutManager = LinearLayoutManager(activity)
        adapterLast = LastMatchAdapter(this.activity!!, 0, events) {
            startActivity<DetailMatchActivity>("idEvent" to it.idEvent)
        }
        lv_match.adapter = adapterLast

        val apiRepository = ApiRepository()
        val gson = Gson()
        lastMatchPresenter = LastMatchPresenter(this@LastMatchFragment, apiRepository, gson)

        matchswipe.onRefresh {
            lastMatchPresenter.getPrevMatchList(league)
        }
    }

    override fun showLoading() {
        matchProgress.visible()
    }

    override fun hideLoading() {
        matchProgress.invisible()
    }

    override fun showPrevMatchList(data: List<EventsItem>) {
        matchswipe.isRefreshing = false
        events.clear()
        events.addAll(data)
        adapterLast.notifyDataSetChanged()
    }

    override fun onAttachView() {
        lastMatchPresenter.onAttach(this)
    }

    override fun onDettachView() {
        lastMatchPresenter.onDettach()
    }

    override fun onStart() {
        super.onStart()
        onAttachView()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDettachView()
    }
}
