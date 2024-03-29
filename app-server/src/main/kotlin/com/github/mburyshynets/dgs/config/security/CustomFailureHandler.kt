package com.github.mburyshynets.dgs.config.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.RedirectStrategy
import org.springframework.security.web.WebAttributes
import org.springframework.security.web.authentication.AuthenticationFailureHandler

/**
 * @see [org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler]
 * @see [org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler]
 */
class CustomFailureHandler(
    private val defaultFailureUrl: String? = null,
    private val alwaysUseDefaultFailureUrl: Boolean = false,
    private val forwardToDestination: Boolean = false,
    private val allowSessionCreation: Boolean = true,
    private val useReferer: Boolean = true,
    private val redirectStrategy: RedirectStrategy = QueryParamRedirectStrategy.AuthFailure,
) : AuthenticationFailureHandler {

    override fun onAuthenticationFailure(
        req: HttpServletRequest,
        res: HttpServletResponse,
        err: AuthenticationException
    ) {
        val failureUrl = determineFailureUrl(req)

        if (failureUrl == null) {
            logger.debug("Sending 401 Unauthorized error")
            res.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.reasonPhrase)
            return
        }

        saveException(req, err)

        if (forwardToDestination) {
            logger.debug("Forwarding to $failureUrl")
            req.getRequestDispatcher(failureUrl).forward(req, res)
        } else {
            redirectStrategy.sendRedirect(req, res, failureUrl)
        }
    }

    /**
     * Caches the [AuthenticationException] for use in view rendering.
     *
     * If [forwardToDestination] is set to true, request scope will be used,
     * otherwise it will attempt to store the exception in the session. If
     * there is no session and [allowSessionCreation] is true a session will
     * be created. Otherwise, the exception will not be stored.
     */
    private fun saveException(req: HttpServletRequest, err: AuthenticationException) {
        if (forwardToDestination) {
            req.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, err)
            return
        }
        val session = req.getSession(false)
        if (session != null || allowSessionCreation) {
            req.session.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, err)
        }
    }

    private fun determineFailureUrl(req: HttpServletRequest): String? {
        if (alwaysUseDefaultFailureUrl) {
            return defaultFailureUrl
        }
        var failureUrl: String? = null
        if (useReferer) {
            failureUrl = req.getHeader("Referer")
            logger.trace("Using url {} from Referer header", failureUrl)
        }
        if (failureUrl.isNullOrBlank()) {
            failureUrl = defaultFailureUrl
            logger.trace("Using default url {}", failureUrl)

        }
        return failureUrl
    }

    companion object {
        private val logger = LoggerFactory.getLogger(CustomFailureHandler::class.java)
    }
}
