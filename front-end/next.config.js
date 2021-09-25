require('dotenv').config();

module.exports = {
  publicRuntimeConfig: {
    BASE_API_URL: process.env.BASE_API_URL,
    EVOLUTION_BASE_URL: process.env.EVOLUTION_BASE_URL,
    NEXT_PUBLIC_RECAPTCHA_SITE_KEY: process.env.NEXT_PUBLIC_RECAPTCHA_SITE_KEY,
  },
  async rewrites() {
    return [
      {
        source: '/api/:path*',
        destination: 'https://document-manager-app.herokuapp.com/api/:path*',
      },
    ]
  },
//   compress: false, // Let Nginx or Apache do it.
//   poweredByHeader: false,
//   images: {
//     domains: [process.env.S3_HOSTNAME_1, process.env.S3_HOSTNAME_2],
//   },
};