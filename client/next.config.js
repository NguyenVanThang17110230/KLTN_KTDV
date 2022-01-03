require('dotenv').config();
module.exports = {
  reactStrictMode: true,
  publicRuntimeConfig: {
    BASE_API_URL: process.env.BASE_API_URL,
    EVOLUTION_BASE_URL: process.env.EVOLUTION_BASE_URL,
    NEXT_PUBLIC_RECAPTCHA_SITE_KEY: process.env.NEXT_PUBLIC_RECAPTCHA_SITE_KEY,
  },
  async rewrites() {
    return [
      {
        source: '/api/:path*',
        destination: 'https://5392-115-79-44-251.ngrok.io/api/:path*',
        // destination: 'https://document-manager-app.herokuapp.com/api/:path*',
      },
    ]
  },
}
