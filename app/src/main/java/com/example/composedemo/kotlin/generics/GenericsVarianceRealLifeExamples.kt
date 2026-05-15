package com.example.composedemo.kotlin.generics

open class FeedItem(
    open val id: String
)

data class Article(
    override val id: String,
    val title: String
) : FeedItem(id)

data class Video(
    override val id: String,
    val url: String
) : FeedItem(id)


// Use out when a type only produces values.
interface FeedRepository<out T : FeedItem> {

    // the generic type (T) is only produced
    suspend fun getItems(): List<T>

    // the generic type (T) cannot be consumed
//    fun consumeItem(item: T)
}

class FeedItemRepository : FeedRepository<FeedItem> {

    override suspend fun getItems(): List<FeedItem> {
        return listOf(
            Article("1", "Kotlin Generics"),
            Article("2", "Jetpack Compose"),
            Video("3", "https://example.com/video.mp4")
        )
    }
}

class ArticleRepository : FeedRepository<Article> {

    override suspend fun getItems(): List<Article> {
        return listOf(
            Article("1", "Kotlin Generics"),
            Article("2", "Jetpack Compose")
        )
    }
}

class VideoRepository : FeedRepository<Video> {

    override suspend fun getItems(): List<Video> {
        return listOf(
            Video("3", "https://example.com/video1"),
            Video("4", "https://example.com/video2")
        )
    }
}

class FeedService(
    private val repository: FeedRepository<FeedItem>
) {

    suspend fun loadFeed() {
        val items = repository.getItems()

        items.forEach {
            println(it.id)
        }
    }
}

fun testOut() {
    val articleRepo: FeedRepository<Article> = ArticleRepository()
    // Expect parent but child works because of covariance -> FeedRepository<Article> subtype of FeedRepository<FeedItem>
    val service: FeedService = FeedService(repository = articleRepo)

    val videoRepo: FeedRepository<Video> = VideoRepository()
    // Expect parent but child works because of covariance -> FeedRepository<Video> subtype of FeedRepository<FeedItem>
    val videoService: FeedService = FeedService(repository = videoRepo)


    val feedItemRepo: FeedRepository<FeedItem> = FeedItemRepository()
    // with no covariance only this works
    val feedService: FeedService = FeedService(repository = feedItemRepo)

    // So we use "out" (covariance) in order not to build ArticleService and VideoService but only FeedService
    // Without "out", you often end up duplicating services for every subtype.
}




open class AnalyticsEvent(
    open val name: String
)

data class ScreenViewEvent(
    override val name: String,
    val screenName: String
) : AnalyticsEvent(name)

data class PurchaseEvent(
    override val name: String,
    val amount: Double
) : AnalyticsEvent(name)


// Use in when a type only consumes values.
interface EventTracker<in T : AnalyticsEvent> {

    // the generic type (T) is only consumed
    fun track(event: T)

    // the generic type (T) cannot be returned
//    fun returnType(): T
}

class FirebaseTracker : EventTracker<AnalyticsEvent> {

    override fun track(event: AnalyticsEvent) {
        println("Tracking event: ${event.name}")
    }
}

class PurchaseViewModel(
    private val tracker: EventTracker<PurchaseEvent>
) {

    fun completePurchase() {
        tracker.track(
            PurchaseEvent(
                name = "purchase_completed",
                amount = 49.99
            )
        )
    }
}

fun testIn() {
    // WITH in You can create ONE generic consumer:
    val firebaseTracker = FirebaseTracker()
    // Expect child but parent works because of contravariance -> EventTracker<AnalyticsEvent> subtype of EventTracker<PurchaseEvent>
    val viewModel = PurchaseViewModel(tracker = firebaseTracker)

    // WITHOUT in You might need even if all trackers do basically the same thing:
//    class PurchaseTracker : EventTracker<PurchaseEvent>
//    class ScreenTracker : EventTracker<ScreenViewEvent>
//    class LoginTracker : EventTracker<LoginEvent>



    // With "out": one service can READ many subtype implementations
    // With "in": one handler can ACCEPT many subtype objects

    // out → flexible producers “I can return more specific things.”
    // in → flexible consumers “I can consume more general things.”
}