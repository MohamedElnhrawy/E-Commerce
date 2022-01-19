package com.gtera.ui.utils

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.IdRes
import androidx.core.widget.ImageViewCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import androidx.viewpager.widget.ViewPager
import com.gtera.R
import com.gtera.ui.adapter.AutoCompleteAdapter
import com.gtera.ui.adapter.BaseAdapter
import com.gtera.ui.adapter.SpinnerAdapter
import com.gtera.ui.base.*
import com.gtera.ui.orders.BasePagedListAdapter
import com.gtera.ui.slider.SliderView
import com.gtera.ui.slider.SliderViewModel
import com.gtera.utils.APPConstants
import com.gtera.utils.Utilities
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.mikelau.views.shimmer.ShimmerRecyclerViewX
import com.mikhaellopez.circularimageview.CircularImageView
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageClickListener
import com.synnapps.carouselview.ImageListener
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.BannerViewPager.OnPageClickListener
import com.zhpan.bannerview.adapter.OnPageChangeListenerAdapter
import com.zhpan.bannerview.holder.HolderCreator
import java.io.ByteArrayOutputStream


object BindingUtils {

    @JvmStatic
    @BindingAdapter("visibleIf")
    fun changeVisibility(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("invisibleIf")
    fun changeInvisibility(view: View, inVisible: Boolean) {
        view.visibility = if (inVisible) View.INVISIBLE else View.VISIBLE
    }

    @JvmStatic
    @BindingAdapter("adapter")
    fun setAdapter(recyclerView: RecyclerView, adapter: BaseAdapter<*>) {
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
    }

    @JvmStatic
    @BindingAdapter(value = ["adapter", "fixedSize"])
    fun setAdapter(recyclerView: RecyclerView, adapter: BaseAdapter<*>, isFixedSize: Boolean) {
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(isFixedSize)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
    }

    @JvmStatic
    @BindingAdapter("adapter")
    fun setAdapter(recyclerView: RecyclerView, adapter: BasePagedListAdapter<*>) {
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
    }

    @JvmStatic
    @SuppressLint("CheckResult")
    @BindingAdapter("imageUrl")
    fun setImageUrl(imageView: ImageView, url: String) {
        if (TextUtils.isEmpty(url)) return
        val context = imageView.context

        val options = Utilities.getImageLoadingOptions(context)

        Glide.with(context).load(url).apply(options).into(imageView)
    }

    @JvmStatic
    @SuppressLint("CheckResult")
    @BindingAdapter("hexColor")
    fun setImageHexColor(imageView: CircularImageView, color: String) {
        if (TextUtils.isEmpty(color)) return
        val context = imageView.context
        imageView.background = (ColorDrawable(Color.parseColor(color)))
    }


    @JvmStatic
    @BindingAdapter(value = ["width", "height"])
    fun setWidthAndHeight(view: View, width: Int, height: Int) {
        var layoutParams = view.layoutParams
        layoutParams.width = width
        layoutParams.height = height
        view.layoutParams = layoutParams
    }

    @JvmStatic
    @BindingAdapter("width")
    fun setLayoutWidth(view: View, newWidth: Int) {
        var layoutParams = view.layoutParams
        layoutParams.width = newWidth
        view.layoutParams = layoutParams
    }

    @JvmStatic
    @BindingAdapter(value = ["imageUri", "roundRadius"], requireAll = false)
    fun setImageUri(imageView: ImageView, uri: Uri?, radius: Int) {
        val context = imageView.context
        val options = Utilities.getImagePlaceholder(context)
        if (radius != 0) options!!.transform(CenterCrop(), RoundedCorners(radius))
        Glide.with(context).load(uri).apply(options!!).into(imageView)
    }

    @JvmStatic
    @SuppressLint("CheckResult")
    @BindingAdapter(
        value = ["imageUrl", "roundRadius", "imageURI", "imageBitmap", "placeHolderImage"],
        requireAll = false
    )
    fun setImageUrl(
        imageView: ImageView,
        url: String?,
        radius: Int,
        imageUri: String?,
        imageBitMap: Bitmap?,
        placeHolder: Int
    ) {


        val context = imageView.context
        val options = Utilities.getImagePlaceholder(context)
        if (radius != 0) options!!.transform(CenterCrop(), RoundedCorners(radius))
        if (imageUri != null && !imageUri.isEmpty()) {

            Glide.with(context)
                .load(imageUri)
                .apply(options!!)
                .into(imageView)

        } else if (url != null && !url.isEmpty() && imageBitMap == null) {

            Glide.with(context)
                .load(url)
                .placeholder(placeHolder)
                .apply(options!!)
                .into(imageView)
        } else if (imageBitMap != null) {

            val stream = ByteArrayOutputStream()
            imageBitMap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            Glide.with(context)
                .load(stream.toByteArray())
                .error(R.drawable.ic_profile_info_placeholder)
                .apply(options!!)
                .into(imageView)
        }


    }


    @JvmStatic
    @SuppressLint("CheckResult")
    @BindingAdapter(value = ["videoUrl"])
    fun setVideoUrl(
        playerView: PlayerView,
        url: String?
    ) {

        var playbackPosition: Long = 0
        var currentWindow: Int = 0
        var playWhenReady: Boolean = true;
        var player: SimpleExoPlayer
//        player = ExoPlayerFactory.newSimpleInstance(
//            playerView.context,
//            DefaultTrackSelector(),
//            DefaultLoadControl()
//        )
//        playerView.player = player
        player = ExoPlayerFactory.newSimpleInstance(playerView.context, DefaultTrackSelector())
        playerView.setPlayer(player)
        playerView.setPlayer(player)

        val dataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(
                playerView.context,
                Util.getUserAgent(
                    playerView.context,
                    "Abaza Auto Trade"
                )
            )
        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse("https://storage.googleapis.com/exoplayer-test-media-0/firebase-animation.mp4"))
        player.prepare(mediaSource)


    }


    private fun buildMediaSource(uri: Uri): MediaSource? {
        val BANDWIDTH_METER = DefaultBandwidthMeter()
        val userAgent = "exoplayer-codelab"

//        return if (uri?.path?.contains("mp3")!! || uri?.path?.contains("mp4")!!) {
//            ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
//                .createMediaSource(uri)
//        } else if (uri?.path?.contains("m3u8")!!) {
//            DashMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
//                .createMediaSource(uri)
//        } else {
//            val dashChunkSourceFactory: DashChunkSource.Factory = DefaultDashChunkSource.Factory(
//                DefaultHttpDataSourceFactory("ua", BANDWIDTH_METER)
//            )
//            val manifestDataSourceFactory: DataSource.Factory =
//                DefaultHttpDataSourceFactory(userAgent)
//            DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory).createMediaSource(uri)
//        }


        val dataSourceFactory = DefaultHttpDataSourceFactory("ua", BANDWIDTH_METER)
        val dashChunkSourceFactory = DefaultDashChunkSource.Factory(dataSourceFactory)
        return DashMediaSource(uri, dataSourceFactory, dashChunkSourceFactory, null, null)

    }

    @JvmStatic
    @BindingAdapter(value = ["list", "changePage", "isLarge"], requireAll = false)
    fun setBannerCreator(
        bannerViewPager: BannerViewPager<*, CustomViewHolder>,
        list: List<*>?,
        changePage: Int,
        isLarge: Boolean
    ) {
        bannerViewPager.setHolderCreator(HolderCreator { CustomViewHolder(isLarge) })

        if (list != null && list.size > 0) bannerViewPager.create(list as List<Nothing>?)

        if (changePage > 0) bannerViewPager.setCurrentItem(changePage) else if (changePage < 0) bannerViewPager.currentItem =
            bannerViewPager.currentItem - 1
    }

    @JvmStatic
    @BindingAdapter(
        value = ["pageChangeListener", "bvp_page_margin", "bvp_reveal_width"],
        requireAll = false
    )
    fun setOnPageChangeListener(
        bannerViewPager: BannerViewPager<*, *>,
        listener: OnPageChangeListenerAdapter?,
        bvp_page_margin: Int,
        bvp_reveal_width: Int
    ) {
        bannerViewPager.setOnPageChangeListener(listener)
        bannerViewPager.setPageMargin(bvp_page_margin)
        bannerViewPager.setRevealWidth(bvp_reveal_width)
    }

    @JvmStatic
    @BindingAdapter("pageClickListener")
    fun setOnPageClickListener(
        bannerViewPager: BannerViewPager<*, *>,
        listener: OnPageClickListener?
    ) {
        bannerViewPager.setOnPageClickListener(listener)
    }

    @JvmStatic
    @BindingAdapter(
        value = ["horizontal_adapter", "ScrollChangeListener", "smoothScrollPosition"],
        requireAll = false
    )
    fun setHorizontalAdapter(
        recyclerView: RecyclerView,
        adapter: BaseAdapter<*>?,
        listener: ScrollChangeListener?,
        smoothScrollPosition: Int
    ) {
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = LinearLayoutManager(
            recyclerView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = adapter
        if (listener != null) recyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                listener.onScrollChange(newState)
            }
        })
        if (smoothScrollPosition != -1) recyclerView.smoothScrollToPosition(smoothScrollPosition)
    }


    @JvmStatic
    @BindingAdapter(
        value = ["horizontal_adapter", "ScrollChangeListener", "smoothScrollPosition"],
        requireAll = false
    )
    fun setHorizontalAdapter(
        recyclerView: RecyclerView,
        adapter: BasePagedListAdapter<*>?,
        listener: ScrollChangeListener?,
        smoothScrollPosition: Int
    ) {
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = LinearLayoutManager(
            recyclerView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = adapter
        if (listener != null) recyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                listener.onScrollChange(newState)
            }
        })
        if (smoothScrollPosition != -1) recyclerView.smoothScrollToPosition(smoothScrollPosition)
    }

    @JvmStatic
    @BindingAdapter("onScrollStarted")
    fun onScrollStarted(
        scrollView: NestedScrollView,
        listener: NestedScrollView.OnScrollChangeListener?
    ) {
        scrollView.setOnScrollChangeListener(listener)
    }

    @JvmStatic
    @BindingAdapter("onScrollStarted")
    fun onScrollStarted(
        scrollView: RecyclerView,
        listener: RecyclerView.OnScrollListener?
    ) {
        scrollView.addOnScrollListener(listener!!)
    }

    @JvmStatic
    @BindingAdapter(value = ["refreshListener", "isRefreshing"])
    fun setOnRefreshListener(
        layout: SwipeRefreshLayout,
        listener: OnRefreshListener?,
        isRefreshing: Boolean
    ) {
        layout.setOnRefreshListener(listener)
        layout.isRefreshing = isRefreshing
    }

    @JvmStatic
    @BindingAdapter("sliderViewModel")
    fun setSliderViewModel(sliderView: SliderView, sliderViewModel: SliderViewModel?) {
        if (sliderView.binding?.getViewModel() == null) sliderView.binding?.setViewModel(
            sliderViewModel
        )
    }


    @JvmStatic
    @BindingAdapter("recyclerSwipe")
    fun resolveRecyclerIssue(
        recyclerView: RecyclerView,
        swipeRefresh: SwipeRefreshLayout
    ) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (recyclerView.layoutManager is LinearLayoutManager) {
                    val pos = (recyclerView.layoutManager as LinearLayoutManager?)
                        ?.findFirstCompletelyVisibleItemPosition()
                    swipeRefresh.isEnabled = pos == 0
                }
            }
        })
    }

    @JvmStatic
    @BindingAdapter(value = ["adapter", "viewOrientation", "spanCount"], requireAll = false)
    fun setAdapter(
        recyclerView: RecyclerView?,
        adapter: BaseAdapter<*>?,
        orientation: ListOrientation?,
        spanCount: Int?
    ) {
        if (orientation == null && adapter == null)
            return

        when (orientation) {
            ListOrientation.ORIENTATION_GRID -> setGridAdapter(
                recyclerView!!, adapter,
                if (spanCount == null || spanCount == -1) 2 else spanCount,
                false
            )
            ListOrientation.ORIENTATION_VERTICAL -> setAdapter(recyclerView!!, adapter!!)
            ListOrientation.ORIENTATION_HORIZONTAL -> setHorizontalAdapter(
                recyclerView!!,
                adapter,
                null,
                -1
            )
            else -> setAdapter(recyclerView!!, adapter!!)
        }
    }


    @JvmStatic
    @BindingAdapter(value = ["gridAdapter", "spanCount", "horizontalGrid"], requireAll = false)
    fun setGridAdapter(
        recyclerView: RecyclerView,
        adapter: BaseAdapter<*>?,
        spanCount: Int,
        horizontalGrid: Boolean
    ) {
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false
        if (horizontalGrid)
            recyclerView.layoutManager = GridLayoutManager(
                recyclerView.context,
                spanCount,
                GridLayoutManager.HORIZONTAL,
                false
            )
        else
            recyclerView.layoutManager = GridLayoutManager(recyclerView.context, spanCount)
    }

    @JvmStatic
    @BindingAdapter("adapter")
    fun setSearchAdapter(
        view: AutoCompleteTextView,
        adapter: AutoCompleteAdapter?
    ) {
        if (adapter != null) view.setAdapter(adapter)
    }

    @JvmStatic
    @BindingAdapter(
        value = ["preformSearch", "searchItemSelect", "searchTextListener"],
        requireAll = false
    )
    fun initializeSearchView(
        view: AutoCompleteTextView,
        listener: SearchActionListener?,
        itemClickListener: OnItemClickListener?,
        suggestListener: EditingActionListener?
    ) {
        if (listener != null)
            view.setOnEditorActionListener { textView: TextView, actionId: Int, event: KeyEvent? ->
                // Identifier of the action. This will be either the identifier you supplied,
                // or EditorInfo.IME_NULL if being called due to the enter key being pressed.
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    (event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
                ) {
                    listener.preformSearch(textView.text.toString())
                    return@setOnEditorActionListener true
                }
                false
            }
        if (itemClickListener != null && view.onItemClickListener == null) view.onItemClickListener =
            itemClickListener

//        if (suggestListener != null)
//            RxTextView.textChanges(view)
//                    .debounce(500, TimeUnit.MILLISECONDS)
//                    .map(new Function<CharSequence, String>() {
//                        @Override
//                        public String apply(CharSequence charSequence) throws Exception {
//                            return charSequence.toString();
//                        }
//                    })
//                    .subscribe(new Consumer<String>() {
//                        @Override
//                        public void accept(String text) throws Exception {
//                            suggestListener.performAction(text);
//                        }
//                    });
        if (suggestListener != null) {
            view.dropDownWidth = view.context.resources.displayMetrics.widthPixels
            val TRIGGER_AUTO_COMPLETE = 100
            val AUTO_COMPLETE_DELAY: Long = 500
            val handler =
                Handler(Handler.Callback { msg: Message ->
                    if (msg.what == TRIGGER_AUTO_COMPLETE) {
                        val text = view.text.toString()
                        if (!TextUtils.isEmpty(text) && text.length >= APPConstants.CONSTANT_SEARCH_SIZE) {
                            suggestListener.performAction(text)
                        }
                    }
                    false
                })
            view.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                }

                override fun afterTextChanged(s: Editable) {
                    handler.removeMessages(TRIGGER_AUTO_COMPLETE)
                    handler.sendEmptyMessageDelayed(
                        TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY
                    )
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }
            })
        }
    }


    @JvmStatic
    @BindingAdapter(value = ["PagingAdapter", "PagingOrientation", "spanCount"], requireAll = false)
    fun setAdapter(
        recyclerView: RecyclerView,
        adapter: BasePagedListAdapter<*>,
        orientation: ListOrientation,
        spanCount: Int?
    ) {
        when (orientation) {
            ListOrientation.ORIENTATION_GRID -> setGridAdapter(recyclerView, adapter, spanCount!!)
            ListOrientation.ORIENTATION_VERTICAL -> setAdapter(recyclerView!!, adapter!!)
            ListOrientation.ORIENTATION_HORIZONTAL -> setHorizontalAdapter(
                recyclerView!!,
                adapter,
                null,
                -1
            )
        }
    }


    @JvmStatic
    @BindingAdapter(value = ["horizentalGridAdapter", "pagingSpanCount"])
    fun setHorizentalGridAdapter(
        recyclerView: RecyclerView,
        adapter: BasePagedListAdapter<*>?,
        spanCount: Int
    ) {

        recyclerView.layoutManager = GridLayoutManager(recyclerView.context, spanCount, GridLayoutManager.HORIZONTAL, false)
//        GravitySnapHelper(Gravity.START).attachToRecyclerView(recyclerView)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false
    }


    @JvmStatic
    @BindingAdapter(value = ["pagingGridAdapter", "pagingSpanCount"])
    fun setGridAdapter(
        recyclerView: RecyclerView,
        adapter: BasePagedListAdapter<*>,
        spanCount: Int
    ) {

        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = GridLayoutManager(recyclerView.context, spanCount)
    }

    @JvmStatic
    @BindingAdapter(value = ["shimmerViewLayout", "shimmerViewCount"])
    fun shimmerViewLayout(
        recyclerView: ShimmerRecyclerViewX,
        @IdRes shimmerLayout: Int,
        count: Int
    ) {
        recyclerView.setDemoLayoutReference(shimmerLayout)
        recyclerView.setDemoChildCount(count)
    }

    @JvmStatic
    @BindingAdapter("shimmerViewLoad")
    fun shimmerViewLoading(
        recyclerView: ShimmerRecyclerViewX,
        isLoading: Boolean
    ) {
        if (isLoading) recyclerView.showShimmerAdapter() else recyclerView.hideShimmerAdapter()
    }

    @JvmStatic
    @BindingAdapter(value = ["layoutMarginStart", "layoutMarginEnd"], requireAll = false)
    fun setLayoutMargins(view: View, start: Float, end: Float) {
        val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.marginStart = start.toInt()
        layoutParams.marginEnd = end.toInt()
        view.layoutParams = layoutParams
    }


    @JvmStatic
    @BindingAdapter(
        value = ["spinnerItems", "enableFirstItem", "spinnerSelectedItem", "spinnerDisabled"],
        requireAll = false
    )
    fun setSpinnerItems(
        spinner: Spinner,
        items: ObservableArrayList<String?>,
        enableFirstItem: Boolean,
        selectedPos: Int,
        spinnerDisabled: Boolean
    ) {
        val adapter = SpinnerAdapter(
            spinner.context,
            R.layout.selector_spinner_header,
            items,
            enableFirstItem
        )
        adapter.setDropDownViewResource(R.layout.simple_spinner_item)
        spinner.adapter = adapter
        if (selectedPos != -1 && selectedPos < items.size && items.size > 0) setSpinnerSelectedItem(
            spinner,
            selectedPos
        )
        spinner.isEnabled = !spinnerDisabled
        spinner.isClickable = !spinnerDisabled
    }

    @JvmStatic
    @BindingAdapter("onEntrySelected")
    fun setSpinnerListener(spinner: Spinner, listener: SpinnerInterface?) {

//
//       if (spinner.onItemClickListener == null)
//           return

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {

                listener?.onClick(position)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    @JvmStatic
    @BindingAdapter("selectedItem")
    fun setSpinnerSelectedItem(spinner: Spinner, pos: Int) {
        spinner.setSelection(pos)
    }

//    @JvmStatic
//    @BindingAdapter("rangeSeekbarChangeListener")
//    fun setRangeSeekbarChangeListener(
//        rangeSeekbar: CrystalRangeSeekbar,
//        listener: OnRangeSeekbarChangeListener?
//    ) {
//        rangeSeekbar.setOnRangeSeekbarChangeListener(listener)
//    }

    @JvmStatic
    @BindingAdapter(value = ["rangeSeekbarChangeListener","seekBarMinValue", "seekBarMaxValue"], requireAll = false)
    fun setRangeSeekbarMinMaxValues(
        rangeSeekbar: CrystalRangeSeekbar,
        listener: OnRangeSeekbarChangeListener?,
        minValue: Float?,
        maxValue: Float?
    ) {
        rangeSeekbar.setMinValue(minValue!!)
        rangeSeekbar.setMaxValue(maxValue!!)
        if(listener!= null)
        rangeSeekbar.setOnRangeSeekbarChangeListener(listener)


    }

    @JvmStatic
    @BindingAdapter("tintColor")
    fun setImageTintColor(
        imageView: ImageView?,
        tintCode: String?
    ) {
        if (TextUtils.isEmpty(tintCode)) {
            ImageViewCompat.setImageTintList(imageView!!, null)
            return
        }
        val color: Int = try {
            Color.parseColor(tintCode)
        } catch (e: IllegalArgumentException) {
            Utilities.getColorFromRes(imageView!!.context, R.color.grey_500)
        }
        ImageViewCompat.setImageTintList(imageView!!, ColorStateList.valueOf(color))
    }

    @JvmStatic
    @BindingAdapter(
        value = ["pageCount", "imageListener", "imageClickListener", "pageChangeListener", "AutoPlay"],
        requireAll = false
    )
    fun initializeCarouselView(
        carouselView: CarouselView,
        count: Int,
        imageListener: ImageListener?,
        imageClickListener: ImageClickListener?,
        onPageChangeListener: ViewPager.OnPageChangeListener?,
        autoPlay: ObservableBoolean?
    ) {
        autoPlay?.let {
            if (it.get())
                carouselView.playCarousel()
            else {
                carouselView.stopCarousel()
            }
        }

        carouselView.setImageListener(imageListener)
        carouselView.setImageClickListener(imageClickListener)
        carouselView.pageCount = count
        carouselView.addOnPageChangeListener(onPageChangeListener)
    }

    @JvmStatic
    @SuppressLint("CheckResult")
    @BindingAdapter(value = ["imageUrl", "roundRadius", "placeHolderImage"], requireAll = false)
    fun setImageUrl(imageView: ImageView, url: String, radius: Int, placeHolder: Int?) {
        if (TextUtils.isEmpty(url)) return
        val context = imageView.context

        val options = Utilities.getImageLoadingOptions(context)
        if (radius != 0)
            options.transform(CenterCrop(), RoundedCorners(radius))

        if (placeHolder != null)
            Glide.with(context).load(url).placeholder(placeHolder).apply(options).into(imageView)
        else
            Glide.with(context).load(url).apply(options).into(imageView)

    }


    @JvmStatic
    @BindingAdapter(value = ["tabAdapter", "currentPos"], requireAll = false)
    fun setTabLayoutAdapter(
        tabLayout: TabLayout,
        list: List<String?>,
        position: Int
    ) {
        if (list.size <= 3) tabLayout.tabMode = TabLayout.MODE_FIXED else tabLayout.tabMode =
            TabLayout.MODE_SCROLLABLE
        if(tabLayout.tabCount<2) {
            tabLayout.removeAllTabs()
            for (item in list) tabLayout.addTab(tabLayout.newTab().setText(item))
        }
        if (position != -1) {
            val tab = tabLayout.getTabAt(position)
            tab?.select()
        }
    }

    @JvmStatic
    @BindingAdapter("tabListener")
    fun setTabLayoutListener(tabLayout: TabLayout, listener: TabLayoutListener) {
        val prevPos = intArrayOf(0)
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position != prevPos[0]) {
                    prevPos[0] = tab.position
                    listener.tabSelected(tab.position)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    @JvmStatic
    @BindingAdapter(
        value = ["imagePath", "isUri", "imageBitmap", "roundRadius"],
        requireAll = false
    )
    fun setImagePath(
        imageView: ImageView?,
        path: String?,
        isUri: Boolean,
        bitmap: Bitmap?,
        radius: Int
    ) {
        if (bitmap != null) imageView?.let {
            setImageBitmap(
                it,
                bitmap, radius
            )
        } else if (isUri) setImageUri(
            imageView!!,
            Uri.parse(path),
            radius
        ) else imageView?.let {
            setImageUrl(it, path!!, radius, 0)
        }
    }


    @JvmStatic
    @BindingAdapter(value = ["imageBitmap", "roundRadius"], requireAll = false)
    fun setImageBitmap(imageView: ImageView, bitmap: Bitmap?, radius: Int?) {
        if (bitmap == null) return

        val context = imageView.context
        val options = Utilities.getImagePlaceholder(context)
        if (radius != 0)
            options?.transform(CenterCrop(), RoundedCorners(radius!!))
        Glide.with(context)
            .load(BitmapDrawable(imageView.context.resources, bitmap))
            .apply(options!!)
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter("webUrl")
    fun setWebUrl(webView: WebView, fileName: String) {
        if (TextUtils.isEmpty(fileName))
            return
        val baseAssetsFolder = if (fileName.startsWith("http")) "" else "file:///android_asset/"
        webView.loadUrl(baseAssetsFolder + fileName)
    }

    @JvmStatic
    @BindingAdapter("isBold")
    fun setBold(view: TextView, isBold: Boolean) {
        if (isBold) {
            view.setTypeface(null, Typeface.BOLD)
        } else {
            view.setTypeface(null, Typeface.NORMAL)
        }
    }


}