const button = document.querySelectorAll(".sendLike");
const post = document.querySelectorAll(".postId");
const likes = document.querySelectorAll(".likesNr");
for (let i=0; i<button.length; i++){
    let card = button[i];
    let id = post[i].value;
    let current_like = likes[i].value;
    card.onclick = function () {
        if (this.style.color === 'lightgray'){
            this.style.color = 'red';
            current_like++;
        } else{
            this.style.color = 'lightgray';
            if (current_like > 0)
                current_like--;
        }
        likes[i].value = current_like;
    };
}

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