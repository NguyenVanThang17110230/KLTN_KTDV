module.exports = {
  purge: ['./pages/**/*.{js,ts,jsx,tsx}', './components/**/*.{js,ts,jsx,tsx}'],
  darkMode: false, // or 'media' or 'class'
  theme: {
    extend: {
      backgroundImage:{
        'background-login': "url('../public/static/img/bg-login.jpg')"
      },
      minWidth: {
        '0': '0',
        '1/3':'33.33%',
        '1/4': '25%',
        '1/2': '50%',
        '3/4': '75%',
        'full': '100%',
       }
    },
  },
  variants: {
    extend: {
      textColor: ['responsive', 'hover', 'focus', 'group-hover'],
      opacity:['responsive', 'hover', 'focus', 'group-hover','disabled'],
      gradientColorStops: ['responsive', 'hover', 'focus','active', 'group-hover'],
      cursor:['responsive', 'hover', 'focus', 'group-hover','disabled'],
      outline:['focus'],
      backgroundOpacity:['responsive', 'hover', 'focus', 'group-hover','disabled']
    },
  },
  plugins: [],
}
