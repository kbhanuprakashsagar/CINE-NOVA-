package com.example.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton providing Retrofit setup, Moshi serialization, and OkHttp client for Indie Film services.
 */
object RetrofitClient {

    private const val BASE_URL = "https://api.cinenova-indie.internal/"

    // Sample curated indie film JSON data served through the OkHttp Interceptor pipeline
    private val INDIE_FILMS_RAW_JSON = """
    [
      {
        "id": "all_we_imagine_as_light",
        "title": "All We Imagine as Light",
        "original_title": "All We Imagine as Light",
        "director": "Payal Kapadia",
        "year": 2024,
        "duration": "1h 58m",
        "language": "Malayalam / Hindi",
        "rating": 8.7,
        "poster_url": "https://lh3.googleusercontent.com/aida-public/AB6AXuDKKTsiFqWA5qhdAn2e8OkGwo0F9uLb054gpl3GXnnDM4QbgT_AtKPGVW2Pqh7AVVzD0SYbSzXM9XQarijjDXbCmi9UGy0yeeUbj9En2MV9fDWwNvhLtvF8UhOsVpu8dwhr1Ciis9fPSgiXgoI8elkzfggI8i7B5mMIinhfwerdt1XOWmAmmUbTOt3OHkVaQiQ4G7cWuCXjYnK1flQaD8iwupOEErapFRZIxs10EK641PKHN0IbKAF14w",
        "backdrop_url": "https://lh3.googleusercontent.com/aida-public/AB6AXuDKKTsiFqWA5qhdAn2e8OkGwo0F9uLb054gpl3GXnnDM4QbgT_AtKPGVW2Pqh7AVVzD0SYbSzXM9XQarijjDXbCmi9UGy0yeeUbj9En2MV9fDWwNvhLtvF8UhOsVpu8dwhr1Ciis9fPSgiXgoI8elkzfggI8i7B5mMIinhfwerdt1XOWmAmmUbTOt3OHkVaQiQ4G7cWuCXjYnK1flQaD8iwupOEErapFRZIxs10EK641PKHN0IbKAF14w",
        "synopsis": "Two Kerala nurses navigating Mumbai's nocturnal tempo journey to a mist-laden coastal town where memory, female longing, and emotional emancipation softly collide under rain-swept neon skies.",
        "genres": ["Auteur Drama", "Poetic Realism"],
        "festival_laurel": "Cannes Grand Prix 2024",
        "streaming_quality": "4K DCI-P3 Master",
        "budget_tier": "International Co-production",
        "critics_consensus": "A radiant, nocturnal masterpiece of sensory cinema.",
        "cinematographer": "Ranabir Das",
        "tags": ["Cannes 2024", "Payal Kapadia", "Nocturnal Mumbai", "Grand Prix Winner"],
        "cast_names": ["Kani Kusruti", "Divya Prabha", "Chhaya Kadam"]
      },
      {
        "id": "bramayugam",
        "title": "Bramayugam: The Age of Madness",
        "original_title": "ഭ്രമയുഗം",
        "director": "Rahul Sadasivan",
        "year": 2024,
        "duration": "2h 19m",
        "language": "Malayalam",
        "rating": 8.5,
        "poster_url": "https://lh3.googleusercontent.com/aida-public/AB6AXuA0qx2aemG5ucTx8xYLXgtMCsvFOESisfl9gRnJNEeTPUMyRjAqHwbmHExzycQlT4eS_rmFI0YREcP8FCu44T_I17GiUTOJkG_kKfcTpGqPTDaFXY4leZXPPaWzf9Qvl9pHojTT-_qcgC2FxsnIRfIrTiZT8poHvrSQfacsc90-kpC2Mv78KVV2kxDU2izJHFniQ2JfDifWph8SFc9MozfHs8Mcmold1Qi1ghD-t47vVa0VtxiTto6S1w",
        "backdrop_url": "https://lh3.googleusercontent.com/aida-public/AB6AXuA0qx2aemG5ucTx8xYLXgtMCsvFOESisfl9gRnJNEeTPUMyRjAqHwbmHExzycQlT4eS_rmFI0YREcP8FCu44T_I17GiUTOJkG_kKfcTpGqPTDaFXY4leZXPPaWzf9Qvl9pHojTT-_qcgC2FxsnIRfIrTiZT8poHvrSQfacsc90-kpC2Mv78KVV2kxDU2izJHFniQ2JfDifWph8SFc9MozfHs8Mcmold1Qi1ghD-t47vVa0VtxiTto6S1w",
        "synopsis": "Set in 17th-century Malabar, a wandering folk singer escaping bonded servitude seeks shelter in a decaying ancestral mansion presided over by a sinister patriarch bound to subterranean Chathan sorcery.",
        "genres": ["Folk Horror", "Monochrome Noir", "Period"],
        "festival_laurel": "Rotterdam Official Selection",
        "streaming_quality": "Monochrome 4K HDR",
        "budget_tier": "Boutique Indie Studio",
        "critics_consensus": "Mammootty's menacing masterclass in claustrophobic monochromatic horror.",
        "cinematographer": "Shehnad Jalal",
        "tags": ["Monochrome", "Mammootty", "Chathan Lore", "Malabar Gothic"],
        "cast_names": ["Mammootty", "Arjun Ashokan", "Sidharth Bharathan"]
      },
      {
        "id": "aattam",
        "title": "Aattam (The Play)",
        "original_title": "ആട്ടം",
        "director": "Anand Ekarshi",
        "year": 2024,
        "duration": "2h 19m",
        "language": "Malayalam",
        "rating": 8.6,
        "poster_url": "https://lh3.googleusercontent.com/aida-public/AB6AXuBjOfkSXzsAPAeE5BSc_5d51dwfL9va090NFP_rzBzrt4f6PpjlThcodTG-pb_4bITaUPa_Ja-Jq2vGxALZwcclIR51XHgNMAZwV5SmTmWVF669tjFTGtxUB2w2aVd0MEd6t0BgZ8ELtyWOoUbJX8a8C5jo38UZqPtuaEdNftclr2aHlSRAPQMd5j7G9Gq9zTaq5EFEdoK9uAOoOESLKNb1MA07EShBABE6_GAhD2w6mWAEVAbrcWraQg",
        "backdrop_url": "https://lh3.googleusercontent.com/aida-public/AB6AXuBjOfkSXzsAPAeE5BSc_5d51dwfL9va090NFP_rzBzrt4f6PpjlThcodTG-pb_4bITaUPa_Ja-Jq2vGxALZwcclIR51XHgNMAZwV5SmTmWVF669tjFTGtxUB2w2aVd0MEd6t0BgZ8ELtyWOoUbJX8a8C5jo38UZqPtuaEdNftclr2aHlSRAPQMd5j7G9Gq9zTaq5EFEdoK9uAOoOESLKNb1MA07EShBABE6_GAhD2w6mWAEVAbrcWraQg",
        "synopsis": "A late-night theatre celebration turns into a psychological trial when the lone actress in a drama troupe alleges harassment, exposing the fracture points of brotherhood, self-preservation, and complicity.",
        "genres": ["Chamber Drama", "Social Realism"],
        "festival_laurel": "Best Feature • 70th National Film Awards",
        "streaming_quality": "4K Ultra HD",
        "budget_tier": "Crowdfunded Collective",
        "critics_consensus": "A devastatingly precise, 12 Angry Men-style study in male fragility.",
        "cinematographer": "Anurag Cherukodu",
        "tags": ["National Award Best Film", "Chamber Drama", "Kerala Theatre"],
        "cast_names": ["Zarin Shihab", "Vinay Forrt", "Kalabhavan Shajohn"]
      },
      {
        "id": "ullozhukku",
        "title": "Ullozhukku (Undercurrent)",
        "original_title": "ഉള്ളൊഴുക്ക്",
        "director": "Christo Tomy",
        "year": 2024,
        "duration": "2h 04m",
        "language": "Malayalam",
        "rating": 8.3,
        "poster_url": "https://lh3.googleusercontent.com/aida-public/AB6AXuCZvvx6JqBUhLQ0ZkBexZ7MWgWkAHHUy2rZl04Xvzfa8WFlUViejxoSWlpH8AiW_JBjg7FFjMhk4aKlqma-wHidKRK2lh89JMnKD70m44xxtZdPLcMnDTUC65nMODpEJlmSukT5tt4mgDGzVEzfJRlBMGZZ2dn_TUnGVa0cG-YLkDZgjdvP5B35n6IBMYz2_G5lGT4W6Cx-ZlAFnNREp-cMNR_c8Ig3GiDquFlvD2gj5CzbXcptztj25A",
        "backdrop_url": "https://lh3.googleusercontent.com/aida-public/AB6AXuCZvvx6JqBUhLQ0ZkBexZ7MWgWkAHHUy2rZl04Xvzfa8WFlUViejxoSWlpH8AiW_JBjg7FFjMhk4aKlqma-wHidKRK2lh89JMnKD70m44xxtZdPLcMnDTUC65nMODpEJlmSukT5tt4mgDGzVEzfJRlBMGZZ2dn_TUnGVa0cG-YLkDZgjdvP5B35n6IBMYz2_G5lGT4W6Cx-ZlAFnNREp-cMNR_c8Ig3GiDquFlvD2gj5CzbXcptztj25A",
        "synopsis": "As torrential monsoon floods submerge the lowlands of Kuttanad, the funeral of a family's only son is halted. Trapped together in rising water, a grieving mother and conflicted daughter-in-law unearth stifling domestic truths.",
        "genres": ["Family Drama", "Monsoon Noir"],
        "festival_laurel": "Kerala State Best Actress Winner",
        "streaming_quality": "4K Atmos",
        "budget_tier": "Independent Production",
        "critics_consensus": "A tour-de-force acting duel between Urvashi and Parvathy Thiruvothu.",
        "cinematographer": "Shehnad Jalal",
        "tags": ["Kuttanad Monsoon", "Urvashi", "Parvathy", "Grief Drama"],
        "cast_names": ["Urvashi", "Parvathy Thiruvothu", "Arjun Radhakrishnan"]
      },
      {
        "id": "nanpakal_nerathu_mayakkam",
        "title": "Nanpakal Nerathu Mayakkam",
        "original_title": "നൻപകൽ നേരത്തു മയക്കം",
        "director": "Lijo Jose Pellissery",
        "year": 2023,
        "duration": "1h 48m",
        "language": "Malayalam / Tamil",
        "rating": 8.2,
        "poster_url": "https://lh3.googleusercontent.com/aida-public/AB6AXuCKXSjJNC2NY1q09BYc2ioi5nb5cv6hJIXBnmAV7Eu2xIMj8v7ogaIDvezKr4kWzmCaFs2dGpiwC4J9Y-e99NkhaV_2vLVn3ffbuvZpT7l6AQxFJnBdO_VHjbIVZatvV-h44ZVeBeueqrc0QoOtRT66cK6svZg87XLExdgpAVtbedpUKKdW6c_nQBpWB-V15ZqhzI7wJkb_SLXubeul_sJpqUqp8sDfvkoPl2c5qFU2prWnpUczVCqv0A",
        "backdrop_url": "https://lh3.googleusercontent.com/aida-public/AB6AXuCKXSjJNC2NY1q09BYc2ioi5nb5cv6hJIXBnmAV7Eu2xIMj8v7ogaIDvezKr4kWzmCaFs2dGpiwC4J9Y-e99NkhaV_2vLVn3ffbuvZpT7l6AQxFJnBdO_VHjbIVZatvV-h44ZVeBeueqrc0QoOtRT66cK6svZg87XLExdgpAVtbedpUKKdW6c_nQBpWB-V15ZqhzI7wJkb_SLXubeul_sJpqUqp8sDfvkoPl2c5qFU2prWnpUczVCqv0A",
        "synopsis": "During a sultry afternoon bus journey returning from Velankanni, a Malayali theatre manager falls into a deep trance-like slumber. Upon waking in rural Tamil Nadu, he assumes the persona and daily life of a missing local villager named Sundaram.",
        "genres": ["Magical Realism", "Auteur Cinema", "Comedy-Drama"],
        "festival_laurel": "IFFK Silver Crow Pheasant Winner",
        "streaming_quality": "4K DCI Calibrated",
        "budget_tier": "Boutique Auteur Feature",
        "critics_consensus": "A mesmerizing, dream-like hypnotic trance fueled by Pellissery's distinct visual geometry.",
        "cinematographer": "Theni Eswar",
        "tags": ["LJP", "IFFK Winner", "Magical Realism", "Mammootty"],
        "cast_names": ["Mammootty", "Ramya Suvi", "Ashokan"]
      },
      {
        "id": "pebbles_koozhangal",
        "title": "Pebbles (Koozhangal)",
        "original_title": "കൂഴങ്കൽ",
        "director": "P.S. Vinothraj",
        "year": 2021,
        "duration": "1h 15m",
        "language": "Tamil",
        "rating": 8.0,
        "poster_url": "https://lh3.googleusercontent.com/aida-public/AB6AXuDF2zD45rhtP73ysB0vmT6Enp4c1-NcJinYLhPizSFRVZO1Yfa0Jcfd2DqjB-gU-A5s93tB6tlQt6HjxD9DUIm2l8y0vTXwScJIkwLNG5ZZk8Sk0yhOmDGon3bzRlokSCT7iK2pgHFUbtT_WWLQj1u-BK7dC0rMygLzAHEgG-o2L3BPHQafcwOg15abgga4Ihs0YbaJtgxt1Ebs7V1R4t8OZBEFcjkoQkv6zVpb0Hp0s9m6uSS-WkeyaQ",
        "backdrop_url": "https://lh3.googleusercontent.com/aida-public/AB6AXuDF2zD45rhtP73ysB0vmT6Enp4c1-NcJinYLhPizSFRVZO1Yfa0Jcfd2DqjB-gU-A5s93tB6tlQt6HjxD9DUIm2l8y0vTXwScJIkwLNG5ZZk8Sk0yhOmDGon3bzRlokSCT7iK2pgHFUbtT_WWLQj1u-BK7dC0rMygLzAHEgG-o2L3BPHQafcwOg15abgga4Ihs0YbaJtgxt1Ebs7V1R4t8OZBEFcjkoQkv6zVpb0Hp0s9m6uSS-WkeyaQ",
        "synopsis": "Under the blistering sun of rural Madurai, an alcoholic, abusive father drags his young son on foot across 13 kilometers of scorched terrain to track down his estranged wife who fled home.",
        "genres": ["Docu-Realism", "Arthouse", "Minimalist"],
        "festival_laurel": "Rotterdam Tiger Award Winner",
        "streaming_quality": "1080p CineDCI",
        "budget_tier": "Micro-Budget Indie",
        "critics_consensus": "A blistering, unvarnished portrait of raw human terrain.",
        "cinematographer": "Vignesh Kumulai",
        "tags": ["Tiger Award", "Rotterdam Winner", "Rowdy Sun", "Oscar Submission"],
        "cast_names": ["Karuththadaiyaan", "Chellapandi"]
      },
      {
        "id": "joji",
        "title": "Joji",
        "original_title": "ജോജി",
        "director": "Dileesh Pothan",
        "year": 2021,
        "duration": "1h 53m",
        "language": "Malayalam",
        "rating": 8.1,
        "poster_url": "https://lh3.googleusercontent.com/aida-public/AB6AXuBj7k3PNMblyg8kjsdZRklGuyQiPLVlJtHPY0ZOyeUtobh32kMg-H1RhAYM-wTC9exdLEXtjkA0RCpnTJzMGvXfneJWofvIcvUvDFAf8DOYLjCZyV9Yt9d8L45XsUtFaJjEfIO7QMcplTVe65p6833rXq8ArXygF5xpgq-lODoLj6IGMMC2gDlFe9Xz4RLA74wInErcsuIT9Xe-k5UsT8MLlXlHtcr_nm07SU00jfmohSg7jWw-lmLgJg",
        "backdrop_url": "https://lh3.googleusercontent.com/aida-public/AB6AXuBj7k3PNMblyg8kjsdZRklGuyQiPLVlJtHPY0ZOyeUtobh32kMg-H1RhAYM-wTC9exdLEXtjkA0RCpnTJzMGvXfneJWofvIcvUvDFAf8DOYLjCZyV9Yt9d8L45XsUtFaJjEfIO7QMcplTVe65p6833rXq8ArXygF5xpgq-lODoLj6IGMMC2gDlFe9Xz4RLA74wInErcsuIT9Xe-k5UsT8MLlXlHtcr_nm07SU00jfmohSg7jWw-lmLgJg",
        "synopsis": "An engineering dropout living under the domineering shadow of his wealthy, tyrannical plantation father is driven to a cold, calculated descent into crime inspired by Shakespeare's Macbeth.",
        "genres": ["Psychological Noir", "Crime Drama", "Adaptation"],
        "festival_laurel": "Swedish International Film Festival Best Feature",
        "streaming_quality": "4K Ultra HD",
        "budget_tier": "Pandemic Auteur Feature",
        "critics_consensus": "A chilling modern re-imagination of Macbeth set in rubber plantations.",
        "cinematographer": "Shyju Khalid",
        "tags": ["Macbeth", "Fahadh Faasil", "Dileesh Pothan", "Syham Pushkaran"],
        "cast_names": ["Fahadh Faasil", "Baburaj", "Shammi Thilakan", "Unnimaya Prasad"]
      },
      {
        "id": "chithha",
        "title": "Chithha",
        "original_title": "ചിത്താ",
        "director": "S.U. Arun Kumar",
        "year": 2023,
        "duration": "2h 20m",
        "language": "Tamil",
        "rating": 8.4,
        "poster_url": "https://lh3.googleusercontent.com/aida-public/AB6AXuAo0PMg5mYvts1JU0vMre23FvRlzWAUCcyU9rrZxDggm676Ny2savJD1rhbVwCsFUOmZGw51mWL5w-5eyAJ1w7usVC8m61Mj3vgyGv4V23K0TqlYG9jzNfclVDMEzeUNP-Neh65dbjwz3lRgGR3-i8VSr_dYbfa0fe25fC0TYYTO1zaa9JhW84j2uUOfOAcPfL7BQe8i9UNoDtdtlsFBT0pijYTP79jGxyPYtm7PMOk4qNn0aL51bcO0A",
        "backdrop_url": "https://lh3.googleusercontent.com/aida-public/AB6AXuAo0PMg5mYvts1JU0vMre23FvRlzWAUCcyU9rrZxDggm676Ny2savJD1rhbVwCsFUOmZGw51mWL5w-5eyAJ1w7usVC8m61Mj3vgyGv4V23K0TqlYG9jzNfclVDMEzeUNP-Neh65dbjwz3lRgGR3-i8VSr_dYbfa0fe25fC0TYYTO1zaa9JhW84j2uUOfOAcPfL7BQe8i9UNoDtdtlsFBT0pijYTP79jGxyPYtm7PMOk4qNn0aL51bcO0A",
        "synopsis": "A loving paternal uncle who raises his niece like his own daughter faces unspeakable emotional trauma and an unforgiving society when she goes missing during a sudden school trip.",
        "genres": ["Social Drama", "Emotional Noir", "Indie Thriller"],
        "festival_laurel": "Filmfare Best Film (Tamil)",
        "streaming_quality": "4K Atmos",
        "budget_tier": "Boutique Indie",
        "critics_consensus": "A deeply sensitive, emotionally harrowing exploration of grief and accountability.",
        "cinematographer": "Balaji Subramanyam",
        "tags": ["Siddharth", "Filmfare Winner", "Emotional Realism"],
        "cast_names": ["Siddharth", "Nimisha Sajayan", "Sahasra Shree"]
      }
    ]
    """.trimIndent()

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    /**
     * Interceptor providing reliable HTTP network mock handling:
     * - Intercepts requests to api.cinenova-indie.internal
     * - Filters by query parameters (category, search, sort)
     * - Returns a 200 OK Response with JSON payload
     */
    private val indieFilmInterceptor = Interceptor { chain ->
        val request = chain.request()
        val url = request.url

        if (url.host == "api.cinenova-indie.internal") {
            val path = url.encodedPath

            if (path.contains("films/indie")) {
                // Parse raw list
                val listType = com.squareup.moshi.Types.newParameterizedType(List::class.java, IndieMovieDto::class.java)
                val adapter = moshi.adapter<List<IndieMovieDto>>(listType)
                val allFilms = adapter.fromJson(INDIE_FILMS_RAW_JSON) ?: emptyList()

                val pathSegments = url.pathSegments
                val isSingleItem = pathSegments.size > 4 && pathSegments[4].isNotBlank()

                val jsonResponse = if (isSingleItem) {
                    val filmId = pathSegments[4]
                    val singleFilm = allFilms.find { it.id == filmId } ?: allFilms.first()
                    moshi.adapter(IndieMovieDto::class.java).toJson(singleFilm)
                } else {
                    val category = url.queryParameter("category")
                    val query = url.queryParameter("query")
                    val sort = url.queryParameter("sort")

                    var filtered = allFilms

                    if (!category.isNullOrBlank() && category != "All" && category != "All Indie") {
                        filtered = when (category) {
                            "Festival Winners" -> filtered.filter { it.festivalLaurel != null }
                            "Auteur Cinema" -> filtered.filter { it.genres.any { g -> g.contains("Auteur", true) || g.contains("Poetic", true) } }
                            "Monochrome & Noir" -> filtered.filter { it.genres.any { g -> g.contains("Monochrome", true) || g.contains("Noir", true) || g.contains("Horror", true) } }
                            "Malayalam Auteur" -> filtered.filter { it.language.contains("Malayalam", true) }
                            "Tamil Realism" -> filtered.filter { it.language.contains("Tamil", true) }
                            else -> filtered.filter { it.genres.any { g -> g.contains(category, true) } }
                        }
                    }

                    if (!query.isNullOrBlank()) {
                        val q = query.lowercase().trim()
                        filtered = filtered.filter {
                            it.title.lowercase().contains(q) ||
                            it.director.lowercase().contains(q) ||
                            it.genres.any { g -> g.lowercase().contains(q) } ||
                            it.tags.any { t -> t.lowercase().contains(q) }
                        }
                    }

                    filtered = when (sort) {
                        "rating" -> filtered.sortedByDescending { it.rating }
                        "year" -> filtered.sortedByDescending { it.year }
                        else -> filtered
                    }

                    adapter.toJson(filtered)
                }

                // Simulate slight network transmission latency for realistic loading experience
                try {
                    Thread.sleep(120)
                } catch (_: InterruptedException) {}

                Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(200)
                    .message("OK")
                    .header("Content-Type", "application/json; charset=utf-8")
                    .header("X-Powered-By", "Retrofit-CineNova-Indie-Engine")
                    .body(jsonResponse.toResponseBody("application/json".toMediaType()))
                    .build()
            } else {
                Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(404)
                    .message("Not Found")
                    .body("{}".toResponseBody("application/json".toMediaType()))
                    .build()
            }
        } else {
            chain.proceed(request)
        }
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(indieFilmInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    val apiService: IndieFilmApiService by lazy {
        retrofit.create(IndieFilmApiService::class.java)
    }
}
