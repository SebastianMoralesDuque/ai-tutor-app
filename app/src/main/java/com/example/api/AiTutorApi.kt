package com.example.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface AiTutorApi {

    @POST("api/users/")
    suspend fun createUser(@Body request: CreateUserRequest): Response<UserResponse>

    @GET("api/users/{user_id}")
    suspend fun getUser(@Path("user_id") userId: String): Response<UserResponse>

    @PATCH("api/users/{user_id}")
    suspend fun updateUser(
        @Path("user_id") userId: String,
        @Body request: UpdateUserRequest
    ): Response<UserResponse>

    @POST("api/daily-session")
    suspend fun getDailySession(@Body request: DailySessionRequest): Response<DailySessionResponse>

    @POST("api/submit-answer")
    suspend fun submitAnswer(@Body request: SubmitAnswerRequest): Response<SubmitAnswerResponse>

    @GET("api/progress/{user_id}")
    suspend fun getProgress(@Path("user_id") userId: String): Response<ProgressResponse>

    @POST("api/chat")
    suspend fun chat(@Body request: ChatRequest): Response<ChatResponse>

    @POST("api/suggestions")
    suspend fun getSuggestions(@Body request: SuggestionsRequest): Response<List<String>>
}
