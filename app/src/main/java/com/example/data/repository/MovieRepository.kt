package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.local.entity.WatchlistEntity
import com.example.data.model.CastMember
import com.example.data.model.LicenseContract
import com.example.data.model.Movie
import com.example.data.model.PlanFeature
import com.example.data.model.PlanSpecRow
import com.example.data.model.StudioOpsLog
import com.example.data.model.SubscriptionPlan
import com.example.data.model.TopStreamingAsset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

object MovieRepository {

    private val initialMovies = listOf(
        Movie(
            id = "manjummel_boys",
            title = "Manjummel Boys",
            subtitle = "Survival Drama • 2024",
            posterUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCKXSjJNC2NY1q09BYc2ioi5nb5cv6hJIXBnmAV7Eu2xIMj8v7ogaIDvezKr4kWzmCaFs2dGpiwC4J9Y-e99NkhaV_2vLVn3ffbuvZpT7l6AQxFJnBdO_VHjbIVZatvV-h44ZVeBeueqrc0QoOtRT66cK6svZg87XLExdgpAVtbedpUKKdW6c_nQBpWB-V15ZqhzI7wJkb_SLXubeul_sJpqUqp8sDfvkoPl2c5qFU2prWnpUczVCqv0A",
            backdropUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuD8fBg99ZDjBV2OgQk_bToDdzvODIkfvNa0THaHrZQoBRq2TXYzW9wu9qdH4-LAwtwEcTkd32aUe9nciCBjRxYPeHRpe_tXt20wkCjKaHDZPK3TRKqVL8qz_aqd05IZnAJr4KUlfra0gCXynQcT9TSXaUhdWR-155SHsSu6MLShBRiQ3QBGyvDofaGWLYcLltccmj5cMoWExC7gYC1VvtRT0YVwcXr1376X4noQPhF0LGLk69CK6lJXwA",
            rating = 8.6,
            voteCount = "74K",
            year = 2024,
            language = "Malayalam",
            genres = listOf("Survival Drama", "Adventure", "Thriller"),
            duration = "2h 15m",
            videoQuality = "4K UHD • HDR10+",
            audioSpec = "Dolby Atmos 7.1",
            synopsis = "A tight-knit group of carefree young friends from Kochi embark on a fun holiday to the misty hills of Kodaikanal. During an unauthorized excursion into the forbidden, treacherous Guna Caves (Devil's Kitchen), one of them plunges into a subterranean abyss. Refusing to leave him behind, his comrades wage an unprecedented, harrowing civilian rescue against all odds.",
            director = "Chidambaram",
            directorRole = "Director / Writer",
            cast = listOf(
                CastMember("Soubin Shahir", "Kuttan / Producer"),
                CastMember("Sreenath Bhasi", "Subhash"),
                CastMember("Balu Varghese", "Sixen"),
                CastMember("Ganapathi", "Dr. Ganapathi"),
                CastMember("Jean Paul Lal", "Siju David")
            ),
            awards = "9 KERALA STATE AWARDS WINNER",
            isTrending = true,
            isOriginal = true,
            matchPercentage = 98,
            progress = 0.55f,
            timeLeft = "58m left",
            downloadSize = "3.8 GB",
            isDownloaded = true,
            isWatchlisted = true,
            userRating = 9.5,
            cinephileReview = "Masterclass in tension & camaraderie",
            boxOfficeMilestone = "First Malayalam film to cross ₹240+ Cr worldwide box office.",
            chapterTitle = "Ch. 4: The Guna Caves Abyss",
            tags = listOf("True Story", "Survival", "Friendship", "Western Ghats")
        ),
        Movie(
            id = "aavesham",
            title = "Aavesham: The Reckoning",
            subtitle = "Action Comedy • 2024",
            posterUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBj7k3PNMblyg8kjsdZRklGuyQiPLVlJtHPY0ZOyeUtobh32kMg-H1RhAYM-wTC9exdLEXtjkA0RCpnTJzMGvXfneJWofvIcvUvDFAf8DOYLjCZyV9Yt9d8L45XsUtFaJjEfIO7QMcplTVe65p6833rXq8ArXygF5xpgq-lODoLj6IGMMC2gDlFe9Xz4RLA74wInErcsuIT9Xe-k5UsT8MLlXlHtcr_nm07SU00jfmohSg7jWw-lmLgJg",
            backdropUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBj7k3PNMblyg8kjsdZRklGuyQiPLVlJtHPY0ZOyeUtobh32kMg-H1RhAYM-wTC9exdLEXtjkA0RCpnTJzMGvXfneJWofvIcvUvDFAf8DOYLjCZyV9Yt9d8L45XsUtFaJjEfIO7QMcplTVe65p6833rXq8ArXygF5xpgq-lODoLj6IGMMC2gDlFe9Xz4RLA74wInErcsuIT9Xe-k5UsT8MLlXlHtcr_nm07SU00jfmohSg7jWw-lmLgJg",
            rating = 8.8,
            voteCount = "92K",
            year = 2024,
            language = "Malayalam",
            genres = listOf("Action Comedy", "Crime", "Masala Indie"),
            duration = "2h 38m",
            videoQuality = "4K Ultra HD",
            audioSpec = "Dolby Atmos 5.1",
            synopsis = "When three engineering freshmen in Bangalore face bullying from tyrannical seniors, they seek out local underworld backup and encounter Ranga—an eccentric, gold-dripping gangster with a heart of gold and unpredictable chaotic energy.",
            director = "Jithu Madhavan",
            directorRole = "Director • Screenplay",
            cast = listOf(
                CastMember("Fahadh Faasil", "Ranga (Gangster)"),
                CastMember("Sajin Gopu", "Amban (Right Hand)"),
                CastMember("Mansoor Ali Khan", "Reddy"),
                CastMember("Hipzster", "Aju")
            ),
            awards = "National Award Nominee • #1 Pan-India Premiere",
            isTrending = true,
            isOriginal = true,
            matchPercentage = 96,
            progress = 0f,
            downloadSize = "2.6 GB",
            isDownloaded = true,
            isWatchlisted = false,
            tags = listOf("Fahadh Faasil", "Bangalore Underworld", "Cult Comedy")
        ),
        Movie(
            id = "bramayugam",
            title = "Bramayugam",
            subtitle = "B&W Folk Horror • 2024",
            posterUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuA0qx2aemG5ucTx8xYLXgtMCsvFOESisfl9gRnJNEeTPUMyRjAqHwbmHExzycQlT4eS_rmFI0YREcP8FCu44T_I17GiUTOJkG_kKfcTpGqPTDaFXY4leZXPPaWzf9Qvl9pHojTT-_qcgC2FxsnIRfIrTiZT8poHvrSQfacsc90-kpC2Mv78KVV2kxDU2izJHFniQ2JfDifWph8SFc9MozfHs8Mcmold1Qi1ghD-t47vVa0VtxiTto6S1w",
            backdropUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuA0qx2aemG5ucTx8xYLXgtMCsvFOESisfl9gRnJNEeTPUMyRjAqHwbmHExzycQlT4eS_rmFI0YREcP8FCu44T_I17GiUTOJkG_kKfcTpGqPTDaFXY4leZXPPaWzf9Qvl9pHojTT-_qcgC2FxsnIRfIrTiZT8poHvrSQfacsc90-kpC2Mv78KVV2kxDU2izJHFniQ2JfDifWph8SFc9MozfHs8Mcmold1Qi1ghD-t47vVa0VtxiTto6S1w",
            rating = 8.5,
            voteCount = "52K",
            year = 2024,
            language = "Malayalam",
            genres = listOf("Folk Horror", "Period Mystery", "Psychological"),
            duration = "2h 19m",
            videoQuality = "Monochrome 4K HDR",
            audioSpec = "Dolby Atmos",
            synopsis = "Set in 17th-century Malabar, a paanan singer escaping slavery stumbles into a decrepit, decaying illam owned by an enigmatic feudal lord, only to find himself trapped in a sinister ritualistic game of psychological dominance and ancestral dark sorcery.",
            director = "Rahul Sadasivan",
            directorRole = "Director • Writer",
            cast = listOf(
                CastMember("Mammootty", "Kodumon Potti"),
                CastMember("Arjun Ashokan", "Thevan"),
                CastMember("Sidharth Bharathan", "Cook")
            ),
            awards = "Rotterdam Official Selection • Critic Laurels",
            isTrending = true,
            isOriginal = true,
            matchPercentage = 99,
            progress = 0.65f,
            timeLeft = "42m left",
            downloadSize = "3.2 GB",
            isDownloaded = false,
            isWatchlisted = true,
            tags = listOf("Monochrome", "Mammootty", "Chathan Lore", "Malabar")
        ),
        Movie(
            id = "kishkindha_kaandam",
            title = "Kishkindha Kaandam",
            subtitle = "Mystery Drama • 2024",
            posterUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDMTfmWwPkypoWxLv7acE9HaxnYWYoVKzjzMGV9K21qTZohcOIUL62PV3bIgBJkBA1aeAqpxb0_7jEzg8QivC5AO2KpmkI5xApP4qU3YXbSR4FK5NgT-xYiS1dz9PUoJiE7bJX4AI_J7iKgvrctqtQIIs8m6zkrcqEOTZ6Qlf7R-IdIOVMIZ0LiZjvz9mmaovgnw-XBIbPwZPoXhMqiMHedLyOklQt70VB5GyDU2En7Ny7PSE6Z4UjfuA",
            backdropUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCfUzZXVJvDvNPudOQr7KC7yktoHoY_Km5tdrIZdpYo6UvB1AnEt9TfBqVgpamoTmkNjYdnRWg8SnrR3iLc8Mb8jrpf3jxiMQINMGWh2T8BkI1NHno6TltH0XpG9ENJ06FWQU_m6dQF5e57fh05WhkMpvlFmQmfa5LhEQ6qC7xtT7ZANoa19-IyzHMZT8HDDhIj1wScH6x4Esf1YA2WgWkFWu2Cmtxkl2DNKnY-H5_le32_lRgqzCQAyA",
            rating = 8.4,
            voteCount = "38K",
            year = 2024,
            language = "Malayalam",
            genres = listOf("Forest Mystery", "Drama", "Investigation"),
            duration = "2h 05m",
            videoQuality = "1080p FHD Cine Master",
            audioSpec = "5.1 Spatial Audio",
            synopsis = "In an isolated forested reserve populated by roaming monkey troops, a retired army officer with progressing memory loss loses his licensed firearm right before general elections. As an investigation unfolds, painful secrets of his past begin to resurface.",
            director = "Dinjith Ayyathan",
            directorRole = "Director",
            cast = listOf(
                CastMember("Asif Ali", "Ajay Chandran"),
                CastMember("Vijayaraghavan", "Appu Pillai"),
                CastMember("Aparna Balamurali", "Aparna")
            ),
            awards = "Critically Acclaimed 2024",
            isTrending = true,
            festPickLabel = "Critically Acclaimed",
            matchPercentage = 95,
            progress = 0f,
            downloadSize = "2.7 GB",
            isDownloaded = false,
            isDownloading = true,
            downloadProgress = 0.68f,
            isWatchlisted = true,
            tags = listOf("Forest Reserve", "Memory Loss", "Whodunit")
        ),
        Movie(
            id = "all_we_imagine_as_light",
            title = "All We Imagine as Light",
            subtitle = "Dir. Payal Kapadia • Malayalam / Hindi",
            posterUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDKKTsiFqWA5qhdAn2e8OkGwo0F9uLb054gpl3GXnnDM4QbgT_AtKPGVW2Pqh7AVVzD0SYbSzXM9XQarijjDXbCmi9UGy0yeeUbj9En2MV9fDWwNvhLtvF8UhOsVpu8dwhr1Ciis9fPSgiXgoI8elkzfggI8i7B5mMIinhfwerdt1XOWmAmmUbTOt3OHkVaQiQ4G7cWuCXjYnK1flQaD8iwupOEErapFRZIxs10EK641PKHN0IbKAF14w",
            backdropUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDKKTsiFqWA5qhdAn2e8OkGwo0F9uLb054gpl3GXnnDM4QbgT_AtKPGVW2Pqh7AVVzD0SYbSzXM9XQarijjDXbCmi9UGy0yeeUbj9En2MV9fDWwNvhLtvF8UhOsVpu8dwhr1Ciis9fPSgiXgoI8elkzfggI8i7B5mMIinhfwerdt1XOWmAmmUbTOt3OHkVaQiQ4G7cWuCXjYnK1flQaD8iwupOEErapFRZIxs10EK641PKHN0IbKAF14w",
            rating = 8.7,
            voteCount = "24K",
            year = 2024,
            language = "Malayalam / Hindi",
            genres = listOf("Poetic Cinema", "Drama", "Auteur Indie"),
            duration = "1h 58m",
            videoQuality = "4K DCI-P3 Color Calibrated",
            audioSpec = "Dolby Atmos",
            synopsis = "Two Kerala nurses living together in nocturnal Mumbai navigate longing, intimacy, and the city's relentless pulse before journeying to a secluded beach town where the boundaries of reality and emotional freedom gently dissolve.",
            director = "Payal Kapadia",
            directorRole = "Director • Writer",
            cast = listOf(
                CastMember("Kani Kusruti", "Prabha"),
                CastMember("Divya Prabha", "Anu"),
                CastMember("Chhaya Kadam", "Parvaty"),
                CastMember("Hridhu Haroon", "Shiaz")
            ),
            awards = "CANNES GRAND PRIX WINNER 2024",
            isFestivalExclusive = true,
            festPickLabel = "Cannes Grand Prix",
            isTrending = true,
            matchPercentage = 97,
            downloadSize = "3.1 GB",
            isWatchlisted = true,
            tags = listOf("Cannes Winner", "Payal Kapadia", "Kani Kusruti")
        ),
        Movie(
            id = "iratta",
            title = "Iratta",
            subtitle = "Investigative Noir • 2023",
            posterUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDF2zD45rhtP73ysB0vmT6Enp4c1-NcJinYLhPizSFRVZO1Yfa0Jcfd2DqjB-gU-A5s93tB6tlQt6HjxD9DUIm2l8y0vTXwScJIkwLNG5ZZk8Sk0yhOmDGon3bzRlokSCT7iK2pgHFUbtT_WWLQj1u-BK7dC0rMygLzAHEgG-o2L3BPHQafcwOg15abgga4Ihs0YbaJtgxt1Ebs7V1R4t8OZBEFcjkoQkv6zVpb0Hp0s9m6uSS-WkeyaQ",
            backdropUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuB8hQkYIe6AmDKU60CNMdBsIzi3N1ZBwxm1xtxFbIkny3E0sxkJdjWpuBTspOOAGwfS541dyAbdvOLBiKzhR_jqpOZ3P_hkB1G2PRaIHIUzyEn0XcPSuxjrxnNsSjhkHlKY52WcCB3ewLEuii4XF6QFxAQSt317ErTo30ZstLCt6NemRvEbSPWgzZRzLIU0q9_NKZoZ5AnIoo19Kp5lSM4LLiJdQ3aeJctcBXOorCdvo5G3yRy7AyWuMQ",
            rating = 8.2,
            voteCount = "45K",
            year = 2023,
            language = "Malayalam",
            genres = listOf("Crime Thriller", "Police Procedural", "Neo-Noir"),
            duration = "1h 55m",
            videoQuality = "4K UHD",
            audioSpec = "5.1 Surround",
            synopsis = "Twin police officers with deeply estranged personalities and unresolved childhood scars collide when one of them is found dead inside the police station with three gunshot wounds on the eve of a ministerial VIP transit.",
            director = "Rohit M.G. Krishnan",
            directorRole = "Director",
            cast = listOf(
                CastMember("Joju George", "Vinod / Pramod (Dual Role)"),
                CastMember("Anjali", "Malini"),
                CastMember("Srinda", "Pramod's Wife")
            ),
            awards = "Kerala State Special Jury Award",
            isTrending = false,
            festPickLabel = "Fest Pick",
            matchPercentage = 98,
            progress = 1.0f,
            downloadSize = "2.1 GB",
            isDownloaded = true,
            isWatchlisted = false,
            userRating = 9.2,
            tags = listOf("Dual Role", "Police Station", "Moral Twists")
        ),
        Movie(
            id = "kaithi",
            title = "Kaithi",
            subtitle = "Action Neo-Noir • 2019",
            posterUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuB0ky--7z2-Z9lHjEWufcqBoEBE0Fcr0eNXDVkEOIRy5rYQhduz9Sa8kfJk8syadkD1QwN7-trnf-qXoBOjVqJbSJqwX3FKvAh9DcCtdv6e42dOUQ2xV5ewEPSfaG0--YGCyRa-Y5ToqSCC4hryAF5sZIAAnp9IxlJJtQNncHabm52mLOsGmouGLfuboKcpg6f3gFZYC970qptuQkr1MFxxt2zBfTJoIPXBNI5XU6YBnnUJtFBNMpTEYA",
            backdropUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuB0ky--7z2-Z9lHjEWufcqBoEBE0Fcr0eNXDVkEOIRy5rYQhduz9Sa8kfJk8syadkD1QwN7-trnf-qXoBOjVqJbSJqwX3FKvAh9DcCtdv6e42dOUQ2xV5ewEPSfaG0--YGCyRa-Y5ToqSCC4hryAF5sZIAAnp9IxlJJtQNncHabm52mLOsGmouGLfuboKcpg6f3gFZYC970qptuQkr1MFxxt2zBfTJoIPXBNI5XU6YBnnUJtFBNMpTEYA",
            rating = 8.5,
            voteCount = "80K",
            year = 2019,
            language = "Tamil",
            genres = listOf("Action Neo-Noir", "Thriller", "LCU"),
            duration = "2h 25m",
            videoQuality = "4K Dolby Vision",
            audioSpec = "Dolby Atmos",
            synopsis = "A paroled prisoner on a solitary mission to meet his daughter for the very first time is commandeered by an injured cop to drive a lorry filled with unconscious poisoned police officers across an ambush-filled highway through the dead of night.",
            director = "Lokesh Kanagaraj",
            directorRole = "Director",
            cast = listOf(
                CastMember("Karthi", "Dilli"),
                CastMember("Narain", "Inspector Bejoy"),
                CastMember("Arjun Das", "Anbu")
            ),
            awards = "LCU Foundation Stone",
            isTrending = true,
            festPickLabel = "LCU Anchor",
            matchPercentage = 94,
            downloadSize = "2.9 GB",
            isWatchlisted = true,
            tags = listOf("Single Night", "Lokesh Cinematic Universe", "Karthi")
        ),
        Movie(
            id = "garudan",
            title = "Garudan",
            subtitle = "Legal Thriller • 2023",
            posterUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAhN4BKl1yVgUPpBMFuwnZp0HjJh5l1TCpnd0DvhjooGG-4LhBIAbmROa1mOeiLqFf2iQiNm1t5B6eFfxRY-wa_ogf5sTYI22mcSq3xTPUy8yRSJ8jxxHYC_rsTvvNUIar3xHV7muDMtO0NC8W4AmCxPV8bgNVQRO-DNQsrvS-w63IKNdYtruA5iNeHwwmJA9xOuugQjytFDkkxyZWj7vwagF4maJnkZ--PIgJyO6f2vUzto5CTkSqzag",
            backdropUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAhN4BKl1yVgUPpBMFuwnZp0HjJh5l1TCpnd0DvhjooGG-4LhBIAbmROa1mOeiLqFf2iQiNm1t5B6eFfxRY-wa_ogf5sTYI22mcSq3xTPUy8yRSJ8jxxHYC_rsTvvNUIar3xHV7muDMtO0NC8W4AmCxPV8bgNVQRO-DNQsrvS-w63IKNdYtruA5iNeHwwmJA9xOuugQjytFDkkxyZWj7vwagF4maJnkZ--PIgJyO6f2vUzto5CTkSqzag",
            rating = 7.9,
            voteCount = "31K",
            year = 2023,
            language = "Malayalam",
            genres = listOf("Legal Thriller", "Courtroom", "Mind Game"),
            duration = "2h 19m",
            videoQuality = "1080p FHD",
            audioSpec = "5.1 Surround",
            synopsis = "A decorated police officer nearing retirement and a razor-sharp university professor wage an intellectual war of attrition inside and outside the courtroom over a decade-old unsolved assault case.",
            director = "Arun Varma",
            directorRole = "Director",
            cast = listOf(
                CastMember("Suresh Gopi", "Harish Madhav IPS"),
                CastMember("Biju Menon", "Nishanth Kumar"),
                CastMember("Abhirami", "Sridevi")
            ),
            awards = "Box Office Superhit",
            isTrending = false,
            festPickLabel = "Legal Thriller",
            matchPercentage = 89,
            downloadSize = "2.4 GB",
            isWatchlisted = true,
            tags = listOf("Courtroom", "Suresh Gopi", "Ego Clash")
        ),
        Movie(
            id = "kantara",
            title = "Kantara: A Legend",
            subtitle = "Folk Lore & Myth • 2022",
            posterUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAoR5jyk1Wg4opPfNIaJvfBvUA0wmm_cyco6StTQklQYauB5w6u706SkBcK_JkLuKUAIqKnD-v9A93pxWYZxmRFDEcsLxxE5x_JlbLx1whTze52f5ug-ti4Msg4zSAk0ojC3FLvripDr6kn5WGs4hdIXVO_Mkfo0AoaNm6kj-rLJCoOqs4_wxocJu06AK1r1uuWT3TJjAh42zHsnnBYnkd_cJGH9uJYGznYQ7vwp7sVJIatEiEgrX2QpA",
            backdropUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAoR5jyk1Wg4opPfNIaJvfBvUA0wmm_cyco6StTQklQYauB5w6u706SkBcK_JkLuKUAIqKnD-v9A93pxWYZxmRFDEcsLxxE5x_JlbLx1whTze52f5ug-ti4Msg4zSAk0ojC3FLvripDr6kn5WGs4hdIXVO_Mkfo0AoaNm6kj-rLJCoOqs4_wxocJu06AK1r1uuWT3TJjAh42zHsnnBYnkd_cJGH9uJYGznYQ7vwp7sVJIatEiEgrX2QpA",
            rating = 8.3,
            voteCount = "98K",
            year = 2022,
            language = "Kannada",
            genres = listOf("Folk Lore & Myth", "Action", "Spiritual"),
            duration = "2h 28m",
            videoQuality = "4K UHD Atmos",
            audioSpec = "Dolby Atmos",
            synopsis = "In the coastal rainforests of Karnataka, a clash between a fiery rebel villager and an upright forest officer erupts over ancestral land rights, evoking the divine fury of the Bhoota Kola demigod spirits.",
            director = "Rishab Shetty",
            directorRole = "Director • Lead Actor",
            cast = listOf(
                CastMember("Rishab Shetty", "Shiva / Demigod Vessel"),
                CastMember("Sapthami Gowda", "Leela"),
                CastMember("Kishore", "Muralidhar")
            ),
            awards = "National Film Award Best Actor",
            isTrending = true,
            matchPercentage = 98,
            progress = 0.40f,
            timeLeft = "1h 10m left",
            downloadSize = "3.4 GB",
            isWatchlisted = true,
            tags = listOf("Bhoota Kola", "Divine Fury", "Coastal Karnataka")
        ),
        Movie(
            id = "aattam",
            title = "Aattam (The Play)",
            subtitle = "National Award Winner • Drama",
            posterUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBjOfkSXzsAPAeE5BSc_5d51dwfL9va090NFP_rzBzrt4f6PpjlThcodTG-pb_4bITaUPa_Ja-Jq2vGxALZwcclIR51XHgNMAZwV5SmTmWVF669tjFTGtxUB2w2aVd0MEd6t0BgZ8ELtyWOoUbJX8a8C5jo38UZqPtuaEdNftclr2aHlSRAPQMd5j7G9Gq9zTaq5EFEdoK9uAOoOESLKNb1MA07EShBABE6_GAhD2w6mWAEVAbrcWraQg",
            backdropUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBjOfkSXzsAPAeE5BSc_5d51dwfL9va090NFP_rzBzrt4f6PpjlThcodTG-pb_4bITaUPa_Ja-Jq2vGxALZwcclIR51XHgNMAZwV5SmTmWVF669tjFTGtxUB2w2aVd0MEd6t0BgZ8ELtyWOoUbJX8a8C5jo38UZqPtuaEdNftclr2aHlSRAPQMd5j7G9Gq9zTaq5EFEdoK9uAOoOESLKNb1MA07EShBABE6_GAhD2w6mWAEVAbrcWraQg",
            rating = 8.6,
            voteCount = "29K",
            year = 2024,
            language = "Malayalam",
            genres = listOf("Chamber Drama", "Social Realism", "Mystery"),
            duration = "2h 19m",
            videoQuality = "4K Master",
            audioSpec = "5.1 Surround",
            synopsis = "Following a theatre celebration at a secluded resort, the lone actress in the troupe alleges sexual harassment by one of the male artists. As the 12 men gather secretly to deliberate her accusation, their solidarity fractures under self-interest, misogyny, and shifting loyalties.",
            director = "Anand Ekarshi",
            directorRole = "Director • National Award",
            cast = listOf(
                CastMember("Zarin Shihab", "Anjali"),
                CastMember("Vinay Forrt", "Vinay"),
                CastMember("Kalabhavan Shajohn", "Hari")
            ),
            awards = "BEST FEATURE FILM • 70TH NATIONAL AWARDS",
            isFestivalExclusive = true,
            isTrending = true,
            matchPercentage = 97,
            progress = 0.32f,
            timeLeft = "1h 14m left",
            downloadSize = "2.8 GB",
            isWatchlisted = true,
            tags = listOf("National Award Best Film", "Chamber Drama", "12 Angry Men Homage")
        ),
        Movie(
            id = "thallumaala",
            title = "Thallumaala",
            subtitle = "Stylized Action • 4K Atmos",
            posterUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCxs0Tcc3mEUa3xUNwTG4onaHGTQP0lUKJSZfaG2vCeRU05A_PnVoC8FvNk3ueOJ8aVb_FWWRkdr8nWs3WKBXMwURpYMy_JfggFi2bQOeZZLSGc091F3xnyJ14nw_LC5bruwB3LDSuvPJLT6NXvPJ7Z1__OSwJoMhY46RvVjFy4d5atz4aEibRB5qBbhhP_FDnZX0mWglM374egWJ8B70773dfDPN7u0qGndt7Xq3iEwqDBP2DLsmnWRw",
            backdropUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCxs0Tcc3mEUa3xUNwTG4onaHGTQP0lUKJSZfaG2vCeRU05A_PnVoC8FvNk3ueOJ8aVb_FWWRkdr8nWs3WKBXMwURpYMy_JfggFi2bQOeZZLSGc091F3xnyJ14nw_LC5bruwB3LDSuvPJLT6NXvPJ7Z1__OSwJoMhY46RvVjFy4d5atz4aEibRB5qBbhhP_FDnZX0mWglM374egWJ8B70773dfDPN7u0qGndt7Xq3iEwqDBP2DLsmnWRw",
            rating = 8.1,
            voteCount = "55K",
            year = 2022,
            language = "Malayalam",
            genres = listOf("Stylized Action", "Hyper-Pop", "Comedy"),
            duration = "2h 28m",
            videoQuality = "4K Atmos",
            audioSpec = "Dolby Atmos 7.1",
            synopsis = "A non-linear, hyper-stylized brawl-fest documenting the intertwined lives of young men in Malabar whose wedding celebrations, internet feuds, and accidental clashes spiral into cinematic combat poetry.",
            director = "Khalid Rahman",
            directorRole = "Director",
            cast = listOf(
                CastMember("Tovino Thomas", "Wasim"),
                CastMember("Kalyani Priyadarshan", "Beepathu"),
                CastMember("Shine Tom Chacko", "SI Reji")
            ),
            isTrending = true,
            matchPercentage = 92,
            progress = 0.82f,
            timeLeft = "25m left",
            downloadSize = "3.5 GB",
            isWatchlisted = false,
            tags = listOf("Tovino Thomas", "Hyper-Action", "Malabar Swag")
        ),
        Movie(
            id = "ullozhukku",
            title = "Ullozhukku",
            subtitle = "Drama • Malayalam",
            posterUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCZvvx6JqBUhLQ0ZkBexZ7MWgWkAHHUy2rZl04Xvzfa8WFlUViejxoSWlpH8AiW_JBjg7FFjMhk4aKlqma-wHidKRK2lh89JMnKD70m44xxtZdPLcMnDTUC65nMODpEJlmSukT5tt4mgDGzVEzfJRlBMGZZ2dn_TUnGVa0cG-YLkDZgjdvP5B35n6IBMYz2_G5lGT4W6Cx-ZlAFnNREp-cMNR_c8Ig3GiDquFlvD2gj5CzbXcptztj25A",
            backdropUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCZvvx6JqBUhLQ0ZkBexZ7MWgWkAHHUy2rZl04Xvzfa8WFlUViejxoSWlpH8AiW_JBjg7FFjMhk4aKlqma-wHidKRK2lh89JMnKD70m44xxtZdPLcMnDTUC65nMODpEJlmSukT5tt4mgDGzVEzfJRlBMGZZ2dn_TUnGVa0cG-YLkDZgjdvP5B35n6IBMYz2_G5lGT4W6Cx-ZlAFnNREp-cMNR_c8Ig3GiDquFlvD2gj5CzbXcptztj25A",
            rating = 8.3,
            voteCount = "21K",
            year = 2024,
            language = "Malayalam",
            genres = listOf("Family Drama", "Monsoon Noir", "Realism"),
            duration = "2h 04m",
            videoQuality = "4K UHD",
            audioSpec = "5.1 Surround",
            synopsis = "During catastrophic monsoon floods in Kuttanad, a deceased man's burial is delayed as the flood waters rise, forcing his grieving mother and young widow to stay cooped up while suffocating secrets slowly surface.",
            director = "Christo Tomy",
            directorRole = "Director",
            cast = listOf(
                CastMember("Urvashi", "Leelamma"),
                CastMember("Parvathy Thiruvothu", "Anju"),
                CastMember("Arjun Radhakrishnan", "Thomaskutty")
            ),
            awards = "Kerala State Best Actress (Urvashi)",
            isTrending = false,
            matchPercentage = 95,
            downloadSize = "2.3 GB",
            isWatchlisted = true,
            tags = listOf("Kuttanad Monsoon", "Urvashi Masterclass", "Grief")
        ),
        Movie(
            id = "viduthalai_1",
            title = "Viduthalai Part 1",
            subtitle = "Completed Oct 14 • Tamil",
            posterUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAo0PMg5mYvts1JU0vMre23FvRlzWAUCcyU9rrZxDggm676Ny2savJD1rhbVwCsFUOmZGw51mWL5w-5eyAJ1w7usVC8m61Mj3vgyGv4V23K0TqlYG9jzNfclVDMEzeUNP-Neh65dbjwz3lRgGR3-i8VSr_dYbfa0fe25fC0TYYTO1zaa9JhW84j2uUOfOAcPfL7BQe8i9UNoDtdtlsFBT0pijYTP79jGxyPYtm7PMOk4qNn0aL51bcO0A",
            backdropUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAo0PMg5mYvts1JU0vMre23FvRlzWAUCcyU9rrZxDggm676Ny2savJD1rhbVwCsFUOmZGw51mWL5w-5eyAJ1w7usVC8m61Mj3vgyGv4V23K0TqlYG9jzNfclVDMEzeUNP-Neh65dbjwz3lRgGR3-i8VSr_dYbfa0fe25fC0TYYTO1zaa9JhW84j2uUOfOAcPfL7BQe8i9UNoDtdtlsFBT0pijYTP79jGxyPYtm7PMOk4qNn0aL51bcO0A",
            rating = 9.0,
            voteCount = "60K",
            year = 2023,
            language = "Tamil",
            genres = listOf("Political Drama", "Period", "Police Procedural"),
            duration = "2h 30m",
            videoQuality = "4K Atmos",
            audioSpec = "Dolby Atmos",
            synopsis = "A newly recruited constable torn between moral conscience and oppressive institutional pressure finds himself stationed in a remote mountain encampment tasked with capturing a legendary separatist leader.",
            director = "Vetri Maaran",
            directorRole = "Director",
            cast = listOf(
                CastMember("Soori", "Kumaresan"),
                CastMember("Vijay Sethupathi", "Vaathiyar"),
                CastMember("Bhavani Sre", "Tamilarasi")
            ),
            awards = "IFFI Spotlight • Critic Acclaim",
            isTrending = false,
            matchPercentage = 96,
            downloadSize = "3.3 GB",
            isWatchlisted = false,
            userRating = 9.0,
            cinephileReview = "Vetri Maaran's uncompromising lens",
            tags = listOf("Vetri Maaran", "Soori", "Vijay Sethupathi")
        ),
        Movie(
            id = "kumbalangi_nights",
            title = "Kumbalangi Nights",
            subtitle = "Family Drama • Malayalam",
            posterUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAx2S2v_WipCnBLBvUErBIM-0xRPjV6h23H9goFkXWSt17nw21So97aFbIoCXt0qvi3hDOV8eV6ZEJ-KEPOcXEQ0MvM7XeT0ZHa1FbgL-zeUVQqIDTDbR_UV89pi-U5HMZu01kXlgTyFtA2sxs1Z8dlO-TIWZ6SvrIJYEA_eOuBLpKJHX9n-qbEKUkHw4PV9XK9RJ5CR4Z_8AzdBfthvFOXR3H77nG6ybilLv1ByU7IVK6UlSBWqGJ9Vw",
            backdropUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAx2S2v_WipCnBLBvUErBIM-0xRPjV6h23H9goFkXWSt17nw21So97aFbIoCXt0qvi3hDOV8eV6ZEJ-KEPOcXEQ0MvM7XeT0ZHa1FbgL-zeUVQqIDTDbR_UV89pi-U5HMZu01kXlgTyFtA2sxs1Z8dlO-TIWZ6SvrIJYEA_eOuBLpKJHX9n-qbEKUkHw4PV9XK9RJ5CR4Z_8AzdBfthvFOXR3H77nG6ybilLv1ByU7IVK6UlSBWqGJ9Vw",
            rating = 8.5,
            voteCount = "78K",
            year = 2019,
            language = "Malayalam",
            genres = listOf("Family Drama", "Cult Classic", "Romance"),
            duration = "2h 15m",
            videoQuality = "1080p FHD",
            audioSpec = "5.1 Spatial",
            synopsis = "Four brothers who share a tumultuous, fractured relationship in a dilapidated house on the outskirts of Kumbalangi come together against patriarchal toxicity when love and tragedy strike.",
            director = "Madhu C. Narayanan",
            directorRole = "Director",
            cast = listOf(
                CastMember("Soubin Shahir", "Saji"),
                CastMember("Fahadh Faasil", "Shammi"),
                CastMember("Shane Nigam", "Bobby")
            ),
            awards = "Modern Cult Classic",
            isTrending = false,
            matchPercentage = 98,
            downloadSize = "2.8 GB",
            isWatchlisted = true,
            tags = listOf("Shammi Hero Da", "Backwaters", "Cult Classic")
        ),
        Movie(
            id = "por_thozhil",
            title = "Por Thozhil",
            subtitle = "Serial Killer Noir • 2023",
            posterUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDF2zD45rhtP73ysB0vmT6Enp4c1-NcJinYLhPizSFRVZO1Yfa0Jcfd2DqjB-gU-A5s93tB6tlQt6HjxD9DUIm2l8y0vTXwScJIkwLNG5ZZk8Sk0yhOmDGon3bzRlokSCT7iK2pgHFUbtT_WWLQj1u-BK7dC0rMygLzAHEgG-o2L3BPHQafcwOg15abgga4Ihs0YbaJtgxt1Ebs7V1R4t8OZBEFcjkoQkv6zVpb0Hp0s9m6uSS-WkeyaQ",
            backdropUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDF2zD45rhtP73ysB0vmT6Enp4c1-NcJinYLhPizSFRVZO1Yfa0Jcfd2DqjB-gU-A5s93tB6tlQt6HjxD9DUIm2l8y0vTXwScJIkwLNG5ZZk8Sk0yhOmDGon3bzRlokSCT7iK2pgHFUbtT_WWLQj1u-BK7dC0rMygLzAHEgG-o2L3BPHQafcwOg15abgga4Ihs0YbaJtgxt1Ebs7V1R4t8OZBEFcjkoQkv6zVpb0Hp0s9m6uSS-WkeyaQ",
            rating = 8.1,
            voteCount = "48K",
            year = 2023,
            language = "Tamil",
            genres = listOf("Serial Killer", "Crime Thriller", "Investigation"),
            duration = "2h 26m",
            videoQuality = "HDR10+",
            audioSpec = "Dolby 5.1",
            synopsis = "A reclusive, cynical senior police investigator and an anxious, book-smart rookie cop must team up to track down an elusive serial killer who leaves no forensic traces.",
            director = "Vignesh Raja",
            directorRole = "Director",
            cast = listOf(
                CastMember("R. Sarathkumar", "SP Lokanathan"),
                CastMember("Ashok Selvan", "DSP Prakash"),
                CastMember("Nikhila Vimal", "Veena")
            ),
            festPickLabel = "Wet Darkness",
            matchPercentage = 95,
            downloadSize = "2.6 GB",
            isWatchlisted = false,
            tags = listOf("Mindhunter Style", "Tamil Thriller", "Investigation")
        ),
        Movie(
            id = "thupparivaalan",
            title = "Thupparivaalan",
            subtitle = "Classic Noir • 2017",
            posterUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuB0ky--7z2-Z9lHjEWufcqBoEBE0Fcr0eNXDVkEOIRy5rYQhduz9Sa8kfJk8syadkD1QwN7-trnf-qXoBOjVqJbSJqwX3FKvAh9DcCtdv6e42dOUQ2xV5ewEPSfaG0--YGCyRa-Y5ToqSCC4hryAF5sZIAAnp9IxlJJtQNncHabm52mLOsGmouGLfuboKcpg6f3gFZYC970qptuQkr1MFxxt2zBfTJoIPXBNI5XU6YBnnUJtFBNMpTEYA",
            backdropUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuB0ky--7z2-Z9lHjEWufcqBoEBE0Fcr0eNXDVkEOIRy5rYQhduz9Sa8kfJk8syadkD1QwN7-trnf-qXoBOjVqJbSJqwX3FKvAh9DcCtdv6e42dOUQ2xV5ewEPSfaG0--YGCyRa-Y5ToqSCC4hryAF5sZIAAnp9IxlJJtQNncHabm52mLOsGmouGLfuboKcpg6f3gFZYC970qptuQkr1MFxxt2zBfTJoIPXBNI5XU6YBnnUJtFBNMpTEYA",
            rating = 7.6,
            voteCount = "33K",
            year = 2017,
            language = "Tamil",
            genres = listOf("Sherlockian Noir", "Detective", "Mystery"),
            duration = "2h 40m",
            videoQuality = "1080p FHD",
            audioSpec = "DTS 5.1",
            synopsis = "A brilliant, eccentric private detective takes on the case of a little boy's murdered pet dog, only to unearth a ruthless syndicate of contracted shadow assassins.",
            director = "Mysskin",
            directorRole = "Director",
            cast = listOf(
                CastMember("Vishal", "Kaniyan Poongundran"),
                CastMember("Prasanna", "Manohar"),
                CastMember("Vinay Rai", "Devil")
            ),
            festPickLabel = "Classic Noir",
            matchPercentage = 88,
            downloadSize = "2.9 GB",
            isWatchlisted = false,
            tags = listOf("Mysskin", "Sherlock Holmes Homage")
        )
    )

    private val _movies = MutableStateFlow(initialMovies)
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()

    private val _selectedLanguage = MutableStateFlow("All")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    private val _selectedVibe = MutableStateFlow<String?>(null)
    val selectedVibe: StateFlow<String?> = _selectedVibe.asStateFlow()

    private val _searchQuery = MutableStateFlow("Dark Malayalam investigative thrillers")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isWifiOnlyDownload = MutableStateFlow(true)
    val isWifiOnlyDownload: StateFlow<Boolean> = _isWifiOnlyDownload.asStateFlow()

    private val _autoSyncCinephile = MutableStateFlow(true)
    val autoSyncCinephile: StateFlow<Boolean> = _autoSyncCinephile.asStateFlow()

    // Subscription Plans
    val subscriptionPlans = listOf(
        SubscriptionPlan(
            id = "premium_4k",
            name = "PREMIUM 4K",
            tag = "Ultra Cinephile Experience",
            badge = "ACTIVE PLAN",
            monthlyPrice = "₹149",
            yearlyPrice = "₹799",
            originalYearlyPrice = "₹1,249",
            effectiveMonthly = "₹66/month",
            isCurrent = true,
            isPopular = false,
            features = listOf(
                PlanFeature("4K UHD + Dolby Vision & HDR10+", "Calibrated color grading profiles for master prints"),
                PlanFeature("Dolby Atmos & 5.1 Master Audio", "Uncompressed orchestral film scores & spatial acoustic mix"),
                PlanFeature("4 Simultaneous Screens", "TV, Living Room Projector, Tablet, & Mobile"),
                PlanFeature("Unlimited Offline 4K & FHD Archival", "Direct download across 4 devices"),
                PlanFeature("100% Zero-Ad Cinema Flow", "No preroll, midroll, or sponsored overlay banners"),
                PlanFeature("IFFK / IFFI Festival Premiere Pass", "Includes Rotterdam indie debuts & Director's Cut extras")
            )
        ),
        SubscriptionPlan(
            id = "standard_hd",
            name = "STANDARD HD",
            tag = "Living Room Cinema",
            badge = "Popular",
            monthlyPrice = "₹99",
            yearlyPrice = "₹499",
            originalYearlyPrice = "₹799",
            effectiveMonthly = "₹41/month",
            isCurrent = false,
            isPopular = true,
            features = listOf(
                PlanFeature("1080p Full HD Resolution", "Clean high-definition stream"),
                PlanFeature("5.1 Surround Studio Audio", "Studio mixed spatial sound"),
                PlanFeature("2 Simultaneous Screens (TV + App)", "Family streaming"),
                PlanFeature("Up to 15 Offline Titles", "Stored on device"),
                PlanFeature("Ad-Free Feature Playback", "Festival intros only")
            )
        ),
        SubscriptionPlan(
            id = "mobility",
            name = "CINE MOBILITY",
            tag = "On-The-Go Film Enthusiast",
            badge = null,
            monthlyPrice = "₹49",
            yearlyPrice = "₹299",
            originalYearlyPrice = "₹449",
            effectiveMonthly = "₹25/month",
            isCurrent = false,
            isPopular = false,
            features = listOf(
                PlanFeature("720p HD (Mobile & Tablet Only)", "Optimized for mobile battery & data"),
                PlanFeature("Standard Stereo 2.0 Audio", "Headphone spatialized"),
                PlanFeature("1 Active Screen", "Personal smartphone"),
                PlanFeature("5 Offline Download Slots", "Commute travel cache")
            )
        )
    )

    val planSpecs = listOf(
        PlanSpecRow("Video Quality", "720p HD", "1080p FHD", "4K + Vision"),
        PlanSpecRow("Audio Mix", "Stereo 2.0", "5.1 Studio", "Dolby Atmos"),
        PlanSpecRow("Devices", "Mobile Only", "TV + Mobile", "All + Projector"),
        PlanSpecRow("Streams", "1 Screen", "2 Screens", "4 Screens"),
        PlanSpecRow("Downloads", "5 Slots", "15 Titles", "Unlimited 4K"),
        PlanSpecRow("Ads", "Standard Ads", "Festival Intros", "100% Ad-Free")
    )

    // Studio Telemetry & Licenses
    val licenseContracts = listOf(
        LicenseContract(
            id = "CN-LIC-8921-AWL",
            title = "All We Imagine as Light",
            languageYear = "Malayalam / Hindi • 2024 • Grand Prix",
            posterUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDKKTsiFqWA5qhdAn2e8OkGwo0F9uLb054gpl3GXnnDM4QbgT_AtKPGVW2Pqh7AVVzD0SYbSzXM9XQarijjDXbCmi9UGy0yeeUbj9En2MV9fDWwNvhLtvF8UhOsVpu8dwhr1Ciis9fPSgiXgoI8elkzfggI8i7B5mMIinhfwerdt1XOWmAmmUbTOt3OHkVaQiQ4G7cWuCXjYnK1flQaD8iwupOEErapFRZIxs10EK641PKHN0IbKAF14w",
            rightsOwner = "Chalk & Cheese Films",
            studioSubsidiary = "co. Petit Chaos (Paris)",
            territory = "India & SAARC",
            territoryNote = "SVOD Holdback Excl.",
            licenseModel = "Exclusive SVOD",
            windowNote = "12-Month Window",
            windowDates = "Dec 02, 2023 → Dec 02, 2024",
            daysRemaining = 8,
            technicalRights = "4K UHD / DCI-P3",
            isDownloadAllowed = true,
            status = "Expires in 8 days",
            isExpiringCritical = true
        ),
        LicenseContract(
            id = "CN-LIC-4410-MBY",
            title = "Manjummel Boys",
            languageYear = "Malayalam / Tamil • 2024 • 2h 15m",
            posterUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCKXSjJNC2NY1q09BYc2ioi5nb5cv6hJIXBnmAV7Eu2xIMj8v7ogaIDvezKr4kWzmCaFs2dGpiwC4J9Y-e99NkhaV_2vLVn3ffbuvZpT7l6AQxFJnBdO_VHjbIVZatvV-h44ZVeBeueqrc0QoOtRT66cK6svZg87XLExdgpAVtbedpUKKdW6c_nQBpWB-V15ZqhzI7wJkb_SLXubeul_sJpqUqp8sDfvkoPl2c5qFU2prWnpUczVCqv0A",
            rightsOwner = "Parava Films",
            studioSubsidiary = "Dir. Chidambaram (Soubin Shahir)",
            territory = "Worldwide Excl.",
            territoryNote = "Global Over-The-Top",
            licenseModel = "Exclusive SVOD",
            windowNote = "36-Month Fixed Window",
            windowDates = "Apr 15, 2024 → Apr 14, 2027",
            daysRemaining = 874,
            technicalRights = "4K UHD • Atmos 7.1",
            isDownloadAllowed = true,
            status = "ACTIVE",
            isExpiringCritical = false
        ),
        LicenseContract(
            id = "CN-LIC-3199-AAV",
            title = "Aavesham",
            languageYear = "Malayalam • 2024 • 2h 38m",
            posterUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBj7k3PNMblyg8kjsdZRklGuyQiPLVlJtHPY0ZOyeUtobh32kMg-H1RhAYM-wTC9exdLEXtjkA0RCpnTJzMGvXfneJWofvIcvUvDFAf8DOYLjCZyV9Yt9d8L45XsUtFaJjEfIO7QMcplTVe65p6833rXq8ArXygF5xpgq-lODoLj6IGMMC2gDlFe9Xz4RLA74wInErcsuIT9Xe-k5UsT8MLlXlHtcr_nm07SU00jfmohSg7jWw-lmLgJg",
            rightsOwner = "Anwar Rasheed Ent.",
            studioSubsidiary = "Fahadh Faasil and Friends",
            territory = "India + GCC (Gulf)",
            territoryNote = "MENA Non-Exclusive",
            licenseModel = "Direct Studio Acq.",
            windowNote = "SVOD Tier 1 Priority",
            windowDates = "May 10, 2024 → May 09, 2026",
            daysRemaining = 542,
            technicalRights = "4K Dolby Vision",
            isDownloadAllowed = true,
            status = "ACTIVE",
            isExpiringCritical = false
        ),
        LicenseContract(
            id = "CN-LIC-1804-IRT",
            title = "Iratta",
            languageYear = "Malayalam • 2023 • Crime Thriller",
            posterUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDF2zD45rhtP73ysB0vmT6Enp4c1-NcJinYLhPizSFRVZO1Yfa0Jcfd2DqjB-gU-A5s93tB6tlQt6HjxD9DUIm2l8y0vTXwScJIkwLNG5ZZk8Sk0yhOmDGon3bzRlokSCT7iK2pgHFUbtT_WWLQj1u-BK7dC0rMygLzAHEgG-o2L3BPHQafcwOg15abgga4Ihs0YbaJtgxt1Ebs7V1R4t8OZBEFcjkoQkv6zVpb0Hp0s9m6uSS-WkeyaQ",
            rightsOwner = "Appu Pathu Pappu Prod.",
            studioSubsidiary = "Martin Prakkat Films",
            territory = "India Exclusive",
            territoryNote = "Rest of World: Sub-licensed",
            licenseModel = "SVOD Exclusive",
            windowNote = "21-Month Contract",
            windowDates = "Feb 18, 2023 → Nov 28, 2024",
            daysRemaining = 14,
            technicalRights = "1080p FHD SDR",
            isDownloadAllowed = false,
            status = "Expires in 14 days",
            isExpiringCritical = true
        )
    )

    val topStreamingAssets = listOf(
        TopStreamingAsset(1, "Aavesham", "Anwar Rasheed Ent.", "Malayalam", "1.84M", 84, "4K Dolby"),
        TopStreamingAsset(2, "Kishkindha Kaandam", "Goodwill Entertainments", "Malayalam", "1.21M", 79, "4K HDR"),
        TopStreamingAsset(3, "Kaithi", "Dream Warrior Pictures", "Tamil", "980K", 88, "4K Dolby"),
        TopStreamingAsset(4, "Kantara", "Hombale Films", "Kannada", "870K", 82, "1080p Atmos"),
        TopStreamingAsset(5, "Bramayugam", "Night Shift Studios", "Malayalam", "760K", 91, "B&W 4K HDR")
    )

    val studioLogs = listOf(
        StudioOpsLog(
            "Anand K. (Content Ops) published Kishkindha Kaandam [4K HDR]",
            "Ingest pipeline completed. Encoded to HEVC & AV1 multi-bitrate tiers for production streaming nodes.",
            "12m ago",
            "publish"
        ),
        StudioOpsLog(
            "Automated DRM License Key rotation verified",
            "Widevine L1 & Apple FairPlay keys synchronized across 4 regional CDN clusters (Mumbai, Chennai, Singapore, Frankfurt).",
            "45m ago",
            "security"
        ),
        StudioOpsLog(
            "Payment Gateway Batch Reconciliation Completed",
            "Stripe / UPI Razorpay Gateway reconciled 42,910 recurring subscriptions renewed. Success rate: 99.41%.",
            "1h ago",
            "payment"
        )
    )

    fun getMovie(id: String): Movie? {
        return _movies.value.find { it.id == id } ?: _movies.value.firstOrNull()
    }

    private var _watchlistRepository: WatchlistRepository? = null
    val watchlistRepository: WatchlistRepository? get() = _watchlistRepository

    private var _reviewRepository: ReviewRepository? = null
    val reviewRepository: ReviewRepository? get() = _reviewRepository

    fun initRoomDatabases(context: Context) {
        initWatchlistDatabase(context)
        if (_reviewRepository == null) {
            val db = AppDatabase.getDatabase(context)
            val repo = ReviewRepository(db.reviewDao())
            _reviewRepository = repo
            CoroutineScope(Dispatchers.IO).launch {
                repo.seedInitialReviewsIfEmpty()
            }
        }
    }

    fun initWatchlistDatabase(context: Context) {
        if (_watchlistRepository == null) {
            val db = AppDatabase.getDatabase(context)
            val repo = WatchlistRepository(db.watchlistDao())
            _watchlistRepository = repo

            CoroutineScope(Dispatchers.IO).launch {
                repo.seedInitialFavoritesIfEmpty(_movies.value)
                repo.allWatchlist.collect { savedEntities ->
                    val savedIds = savedEntities.map { it.movieId }.toSet()
                    _movies.update { currentList ->
                        currentList.map { m ->
                            m.copy(isWatchlisted = savedIds.contains(m.id))
                        }
                    }
                }
            }
        }
    }

    fun toggleWatchlist(movieId: String) {
        val targetMovie = _movies.value.find { it.id == movieId }
        val newStatus = !(targetMovie?.isWatchlisted ?: false)

        _movies.update { list ->
            list.map { movie ->
                if (movie.id == movieId) movie.copy(isWatchlisted = newStatus) else movie
            }
        }

        _watchlistRepository?.let { repo ->
            CoroutineScope(Dispatchers.IO).launch {
                if (targetMovie != null) {
                    if (newStatus) {
                        repo.addToWatchlist(targetMovie.copy(isWatchlisted = true))
                    } else {
                        repo.removeFromWatchlist(movieId)
                    }
                }
            }
        }
    }

    fun toggleDownload(movieId: String) {
        _movies.update { list ->
            list.map { movie ->
                if (movie.id == movieId) {
                    if (movie.isDownloaded) {
                        movie.copy(isDownloaded = false, isDownloading = false, downloadProgress = 0f)
                    } else if (movie.isDownloading) {
                        movie.copy(isDownloading = false, isDownloaded = true, downloadProgress = 1.0f)
                    } else {
                        movie.copy(isDownloading = true, downloadProgress = 0.45f)
                    }
                } else movie
            }
        }
    }

    fun removeDownload(movieId: String) {
        _movies.update { list ->
            list.map { movie ->
                if (movie.id == movieId) {
                    movie.copy(isDownloaded = false, isDownloading = false, downloadProgress = 0f)
                } else movie
            }
        }
    }

    fun clearAllDownloads() {
        _movies.update { list ->
            list.map { it.copy(isDownloaded = false, isDownloading = false, downloadProgress = 0f) }
        }
    }

    fun setLanguageFilter(lang: String) {
        _selectedLanguage.value = lang
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectVibe(vibe: String) {
        _selectedVibe.value = if (_selectedVibe.value == vibe) null else vibe
        if (_selectedVibe.value != null) {
            _searchQuery.value = vibe
        }
    }

    fun toggleWifiOnly() {
        _isWifiOnlyDownload.value = !_isWifiOnlyDownload.value
    }

    fun toggleAutoSync() {
        _autoSyncCinephile.value = !_autoSyncCinephile.value
    }

    fun addMoviesIfNotExist(newMovies: List<Movie>) {
        _movies.update { current ->
            val existingIds = current.map { it.id }.toSet()
            val toAdd = newMovies.filter { it.id !in existingIds }
            if (toAdd.isNotEmpty()) current + toAdd else current
        }
    }
}
