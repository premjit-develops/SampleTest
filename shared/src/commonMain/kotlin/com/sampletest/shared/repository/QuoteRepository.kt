package com.sampletest.shared.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Optional
import com.apollographql.apollo.exception.ApolloGraphQLException
import com.apollographql.cache.normalized.FetchPolicy
import com.apollographql.cache.normalized.apolloStore
import com.apollographql.cache.normalized.fetchPolicy
import com.apollographql.cache.normalized.watch
import com.sampletest.shared.DeleteFavoriteQuoteMutation
import com.sampletest.shared.GetFavoriteQuotesQuery
import com.sampletest.shared.GetQuotesQuery
import com.sampletest.shared.GetQuoteDetailByIdQuery
import com.sampletest.shared.InsertFavoriteQuoteMutation
import com.sampletest.shared.model.Book
import com.sampletest.shared.model.BookSection
import com.sampletest.shared.model.FavoriteQuote
import com.sampletest.shared.model.Quote
import com.sampletest.shared.model.QuoteDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import org.koin.core.annotation.Single
import kotlin.coroutines.cancellation.CancellationException


@Single
class QuoteRepository(
    val apolloClient: ApolloClient
) {

    companion object {

        const val USER_ID = "76f39843-b151-4554-ba61-2ced3d393154"
    }

    fun getQuoteDetailById(
        quoteId: Int,
    ): Flow<Result<QuoteDetail?>> = apolloClient
        .query(GetQuoteDetailByIdQuery(quoteId = quoteId, userId = USER_ID))
        .fetchPolicy(FetchPolicy.CacheAndNetwork)
        .watch()
        .map { response ->
            val data = response.requireDataOrThrow()
            data?.quotesCollection?.edges?.firstOrNull()?.node?.toWithFavorite()
        }.asResultFlow()

    fun getQuotes(): Flow<Result<List<Quote>>> = apolloClient
        .query(
            GetQuotesQuery(
                limit = Optional.present(100),
                offset = Optional.present(0)
            )
        )
        .fetchPolicy(FetchPolicy.CacheAndNetwork)
        .watch()
        .mapNotNull { response ->
            val data = response.requireDataOrThrow()
            data?.quotesCollection?.edges?.map { it.node.toQuote() }
        }.asResultFlow()


    fun getFavoriteQuotes(): Flow<Result<List<FavoriteQuote>?>> = apolloClient
        .query(
            GetFavoriteQuotesQuery(
                userId = USER_ID,
                limit = Optional.present(100),
                offset = Optional.present(0)
            )
        )
        .fetchPolicy(FetchPolicy.CacheAndNetwork)
        .watch()
        .mapNotNull { response ->

            val data = response.requireDataOrThrow()
            data?.favoritesCollection?.edges?.map { it.node.toFavoriteQuote() }

        }.asResultFlow()


    suspend fun insertFavorite(
        quoteId: Int,
        quoteText: String,
        quoteMeaning: String,
        bookSections: List<BookSection>
    ): Result<Unit> {
        return try {

            val query = GetFavoriteQuotesQuery(
                userId = USER_ID,
                limit = Optional.present(100),
                offset = Optional.present(0)
            )

            val response =
                apolloClient.query(query).fetchPolicy(FetchPolicy.CacheFirst).execute()


            apolloClient.mutation(InsertFavoriteQuoteMutation(USER_ID, quoteId)).execute()


            val currentEdges = response.data
                ?.favoritesCollection
                ?.edges
                .orEmpty()

            val newEdge = GetFavoriteQuotesQuery.Edge(
                __typename = "favoritesEdge",
                node = GetFavoriteQuotesQuery.Node(
                    __typename = "favorites",
                    id = USER_ID,
                    quote_id = quoteId,

                    quotes = GetFavoriteQuotesQuery.Quotes(
                        __typename = "quotes",
                        id = quoteId,
                        text = quoteText,
                        meaning = quoteMeaning,

                        book_sectionsCollection =
                            GetFavoriteQuotesQuery.Book_sectionsCollection(
                                __typename = "book_sectionsConnection",
                                edges = bookSections.map {
                                    GetFavoriteQuotesQuery.Edge1(
                                        __typename = "book_sectionsEdge",
                                        node = GetFavoriteQuotesQuery.Node1(
                                            __typename = "book_sections",
                                            id = it.id,
                                            books = GetFavoriteQuotesQuery.Books(
                                                __typename = "books",
                                                id = it.book.id,
                                                name = it.book.name
                                            )
                                        )
                                    )
                                }
                            )
                    )
                )
            )


            val updatedEdges = (listOf(newEdge) + currentEdges)
                .distinctBy { it.node.quote_id }

            val newData = response.data?.copy(
                favoritesCollection = response.data?.favoritesCollection?.copy(
                    edges = updatedEdges
                )
            )

            if (newData != null) {
                apolloClient.apolloStore.writeOperation(
                    operation = query,
                    data = newData,
                    publish = true
                )
            }


            Result.success(Unit)


        } catch (e: Exception) {
            Result.failure(e)
        }

    }


    suspend fun deleteFavorite(
        quoteId: Int
    ): Result<Unit> {
        return try {

            val query = GetFavoriteQuotesQuery(
                userId = USER_ID,
                limit = Optional.present(100),
                offset = Optional.present(0)
            )

            val response =
                apolloClient.query(query).fetchPolicy(FetchPolicy.CacheFirst).execute()


            apolloClient.mutation(DeleteFavoriteQuoteMutation(USER_ID, quoteId)).execute()

            val updatedEdges = response.data?.favoritesCollection?.edges
                ?.filterNot { it.node.id == USER_ID && it.node.quote_id == quoteId }.orEmpty()

            val newData = response.data?.copy(
                favoritesCollection = response.data?.favoritesCollection?.copy(
                    edges = updatedEdges
                )
            )

            if (newData != null) {
                apolloClient.apolloStore.writeOperation(
                    operation = query,
                    data = newData,
                    publish = true,

                    )
            }

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }

    }


}


fun GetQuoteDetailByIdQuery.Node.toWithFavorite(): QuoteDetail = QuoteDetail(
    id = id,
    text = text,
    meaning = meaning,

    bookSection = book_sectionsCollection?.edges.orEmpty().map {

        BookSection(
            id = it.node.id,
            pathCode = it.node.path_code.orEmpty(),
            book = Book(
                id = it.node.books?.id ?: 0,
                name = it.node.books?.name.orEmpty(),
                author = it.node.books?.author.orEmpty()

            )
        )
    },
    isFavorite = !favoritesCollection?.edges.isNullOrEmpty()
)


fun GetFavoriteQuotesQuery.Node.toFavoriteQuote(): FavoriteQuote = FavoriteQuote(
    id = id,
    quoteId = quotes?.id ?: 0,
    bookName = quotes?.book_sectionsCollection?.edges
        ?.firstNotNullOfOrNull { it.node.books?.name }.orEmpty(),
    text = quotes?.text.orEmpty()

)


fun GetQuotesQuery.Node.toQuote(): Quote = Quote(
    id = id,
    text = text,
    meaning = meaning
)


fun <D : Operation.Data> ApolloResponse<D>.requireDataOrThrow(): D? {
    errors?.firstOrNull()?.let { error ->
        throw ApolloGraphQLException(error)
    }

    return data
}

fun <T> Flow<T>.asResultFlow(
): Flow<Result<T>> = map<T, Result<T>> { Result.success(it) }
    .catch { throwable ->
        if (throwable is CancellationException) throw throwable
        emit(Result.failure(throwable))
    }
