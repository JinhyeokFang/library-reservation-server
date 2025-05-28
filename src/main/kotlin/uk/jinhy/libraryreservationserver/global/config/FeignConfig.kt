package uk.jinhy.libraryreservationserver.global.config

import feign.Client
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Configuration
@EnableFeignClients(basePackages = ["uk.jinhy.libraryreservationserver.infrastructure"])
class FeignConfig {
    @Bean
    fun feignClient(): Client {
        val trustAllCerts =
            arrayOf<TrustManager>(
                object : X509TrustManager {
                    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()

                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String,
                    ) = Unit

                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String,
                    ) = Unit
                },
            )

        val sslContext =
            SSLContext.getInstance("SSL").apply {
                init(null, trustAllCerts, java.security.SecureRandom())
            }

        return Client.Default(
            sslContext.socketFactory,
        ) { _, _ -> true }
    }
}
