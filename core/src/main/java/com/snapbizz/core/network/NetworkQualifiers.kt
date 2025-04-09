package com.snapbizz.core.network

import javax.inject.Qualifier

class NetworkQualifiers {
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitBaseV2

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitBaseV3

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitBaseV4

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HttpClientV2Client

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HttpClientV3Client

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HttpClientV4Client