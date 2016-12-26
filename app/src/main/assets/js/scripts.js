/* Highlight outline elements */

function showOutlineElements() {
 if (!document.querySelector('.show-outline-elements')) {
  var o = '<style class="show-outline-elements">*,:before,:after{outline:1px solid rgba(255,100,0,0.6)}</style>';
  document.body.insertAdjacentHTML("beforeEnd",o);
 } else document.body.removeChild(document.querySelector('.show-outline-elements'));
}
