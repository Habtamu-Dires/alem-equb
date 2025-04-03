/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors:{
        "coral-red": "#FF6452",
        "grn":'#2a7d6e' /* #1c594e */
      }
    },
  },
  plugins: [],
}

