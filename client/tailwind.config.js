module.exports = {
  purge: ['./pages/**/*.{js,ts,jsx,tsx}', './components/**/*.{js,ts,jsx,tsx}'],
  darkMode: false, // or 'media' or 'class'
  theme: {
    extend: {
      backgroundImage:{
        'background-login': "url('../public/static/img/bg-login.jpg')"
      },
      // fontFamily:{
      //   'body': ['Open Sans', sans-serif]
      // }
    },
  },
  variants: {
    extend: {
      textColor: ['responsive', 'hover', 'focus', 'group-hover'],
      gradientColorStops: ['responsive', 'hover', 'focus','active', 'group-hover']
    },
  },
  plugins: [],
}
