(function($) {
    $(function() {

        $('nav ul li a:not(:only-child)').click(function(e) {
            $(this).siblings('.nav-dropdown').toggle();

            $('.nav-dropdown').not($(this).siblings()).hide();
            e.stopPropagation();
        });

        $('html').click(function() {
            $('.nav-dropdown').hide();
        });

        $('#nav-toggle').click(function() {
            $('nav ul').slideToggle();
        });

        $('#nav-toggle').on('click', function() {
            this.classList.toggle('active');
        });
    });
})(jQuery);


const dismissAll = document.getElementById('dismiss-all');
const dismissBtns = Array.from(document.querySelectorAll('.dismiss-notification'));

const notificationCards = document.querySelectorAll('.notification-card');

dismissBtns.forEach(btn => {
    btn.addEventListener('click', function(e){
        e.preventDefault;
        var parent = e.target.parentElement.parentElement.parentElement.parentElement.parentElement.parentElement;
        parent.classList.add('display-none');
    })
});

dismissAll.addEventListener('click', function(e){
    e.preventDefault;
    notificationCards.forEach(card => {
        card.classList.add('display-none');
    });
    const row = document.querySelector('.notification-container');
    const message = document.createElement('h4');
    message.classList.add('text-center');
    message.innerHTML = 'All caught up!';
    row.appendChild(message);
})