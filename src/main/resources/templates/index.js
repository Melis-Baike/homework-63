const userProfile = document.getElementById('userProfile');
const loginPage = document.getElementById('login-page');
const loginForm = document.getElementById('login-form');
function showSplashScreen(){
    const elem = document.createElement('div');
    elem.setAttribute('id', 'screen');
    elem.setAttribute('class','splash');
    const elemIn = document.createElement('h1');
    elemIn.setAttribute('class', 'splash-header');
    document.getElementById('test').after(elem);
    const splashScreen = document.querySelector('.splash');
    const posts = document.getElementsByClassName('card');
    for(let post of posts){
        post.style.position = 'unset';
    }
    const images = document.getElementsByClassName('like-photo');
    for (let image of images){
        image.style.position = 'unset';
    }
    const button = document.createElement('button');
    button.setAttribute('id', 'btn')
    button.addEventListener('click',() => {
        splashScreen.style.opacity = 0;
        setTimeout(() => {
            splashScreen.classList.add('hidden')
        }, 610)
        for (let post of posts){
            post.style.position = 'relative';
        }
        for (let image of images){
            image.style.position = 'relative';
        }
    })
    elem.append(elemIn);
    button.innerText = 'Войти';
    elemIn.append(button);
}

function hideSplashScreen(){
    document.getElementById('btn').click()
}

function createCommentElement(comment){
    return '<div>\n' +
            comment.text + '<br>\n' +
            new Date() + '<br>\n' +
            comment.userID + '<br>\n' +
            '</div>';
}

function createPostElement(post){
    return '<div class="card" style="width: 18rem;" id="' + post.id + '">\n' +
        '<img class="card-img-top like-photo" src ="' + post.image + '"' +
        'alt="image" id="image">\n' +
        '<div class="card-body" id="card_body">' +
        '<p>\n' +
        post.description + '<br>\n' +
        post.time + '<br>\n' +
        post.user + '<br>\n' +
        '</p>\n' +
        '<span class="h1 mx-2 muted iconArea" id="like_span">\n' +
        '<div>' +
        '<i class="bi bi-heart heart like_icon nestedIconArea" style="color: gray" id="icon"></i>' +
        '<i class="far fa-comment nestedIconArea" id="comment"></i>' +
        '</div>' +
        '<i class="bi bi-bookmark" id="bookmark"></i>\n' +
        '</span>' +
        '</div>' +
        '<form id="form" class="commentArea" style="display: none">'+
            '<p><textarea name="comment" required></textarea></p>' +
            '<input type="hidden" class="commentUserId" id="commentUserId" name="userId">' +
            '<input type="hidden" class="commentPostId" id="commentPostId" name="postId" value="' + post.id +'">' +
            '<button type="submit" id="commentButton">Отправить</button>' +
        '</form>' +
        '<div id="placeForComment" class="placeForComment" style="display: none"></div>' +
        '</div>';
}

function addPost(postElement){
    const nDoc = parse(postElement);
    const card = nDoc.body.getElementsByClassName('card').item(0);
    const posts = document.getElementById('posts');
    const bookmark = nDoc.body.getElementsByClassName('bi bi-bookmark');
    setBookmarkIcon(card, bookmark.item(0));
    const likeIcon = nDoc.body.getElementsByClassName('bi bi-heart');
    setLikeIcon(card, likeIcon.item(0));
    setLikeAnimation(card);
    addComment(card);
    posts.append(card);
}

function parse(element){
    let ndp = new DOMParser();
    return ndp.parseFromString(element, 'text/html');
}

function setLikeIcon(card, icon) {
    icon.addEventListener('click', function (){
        if(!icon.className.includes('fill')){
            icon.className = 'bi bi-heart-fill like_icon heart';
            icon.style.color = 'red';
            sendPublicationLikeToServer(card);
        } else {
            icon.className = 'bi bi-heart like_icon heart'
            icon.style.color = 'gray';
            sendRemovingPublicationLikeToServer(card);
        }
    })
}

function setBookmarkIcon(card, bookmark) {
    bookmark.addEventListener('click', function () {
        if (!bookmark.className.includes('fill')) {
            bookmark.className = 'bi bi-bookmark-fill';
            axios.post('http://127.0.0.1:8089/publications/' + card.id + '/bookmark', '', updateOptions({}))
                .then(function (response){
                    console.log(response)
                }).catch(function (error){
                    console.log(error);
            });
        } else {
            bookmark.className = 'bi bi-bookmark';
            axios.delete('http://127.0.0.1:8089/publications/' + card.id + '/bookmark', updateOptions({}))
                .then(function (response){
                    console.log(response);
                }).catch(function (error){
                    console.log(error);
            });
        }
    });
}

function setLikeAnimation(post) {
    const image = post.getElementsByClassName('like-photo');
    image.item(0).addEventListener('dblclick', () => {
        createLike(post);
    });
}

function createLike(post){
    const heart = document.createElement('i');
    const like = post.getElementsByClassName('heart').item(0);
    like.click();
    if(like.className.includes('fill')){
        heart.className = 'bi bi-heart-fill heart animation';
        heart.style.color = 'red';
        setTimeout(() => heart.remove(), 1000);
        post.getElementsByClassName('like-photo').item(0).parentNode.appendChild(heart);
    }
}

function sendPublicationLikeToServer(card){
    axios.post('http://127.0.0.1:8089/publications/' + card.id + '/like', '', updateOptions({}))
        .then(function (response){
            console.log(response);
        }).catch(function (error){
            console.log(error);
        });
}

function sendRemovingPublicationLikeToServer(card){
    axios.delete('http://127.0.0.1:8089/publications/' + card.id + '/like', updateOptions({}))
        .then(function (response){
            console.log(response);
        }).catch(function (error){
        console.log(error);
    });
}

document.getElementById('updatePhoto').addEventListener('submit', e => postPublication(e));
async function postPublication(e) {
    e.preventDefault();
    const form = e.target;
    const data = new FormData(form);
    const user = await axios.post('http://127.0.0.1:8089/users/auth', '', updateOptions({}));
    document.getElementById('id').value = user.data.id;
    console.log(document.getElementById('id').value);
    const id = await axios.get('http://127.0.0.1:8089/publications', updateOptions({}));
    const update = updateOptions({});
    update.headers['Content-Type'] = 'multipart/form-data';
    axios.post('http://127.0.0.1:8089/publications/post', data
        , update).then(function (response) {
        axios.get('http://127.0.0.1:8089/publications/image/' + response.data, updateOptions({}))
            .then(function (response) {
                nPost = {
                    id : id.data.length + 1,
                    image: response.data,
                    description: data.get('description'),
                    user: user.data.id,
                    time: new Date()
                }
                addPost(createPostElement(nPost));
                initUserIdField();
            })
            .catch(function (error) {
                console.log(error);
            });
    })
        .catch(function (error) {
            console.log(error);
        });
}

async function addComment(post){
    const comment = post.getElementsByClassName('far fa-comment')[0];
    comment.addEventListener('click', async function () {
        const form = post.children.item(2);
        const place = post.getElementsByClassName('placeForComment').item(0);
        if (form.style.display === 'none') {
            const commentArray = await axios.get('http://127.0.0.1:8089/publications/' + post.id + '/comment',
                updateOptions({}));
            place.remove();
            const commentPlace = document.createElement('div');
            commentPlace.className = 'placeForComment';
            commentPlace.id = 'placeForComment';
            post.append(commentPlace);
            for (let i = 0; i < commentArray.data.length; i++) {
                const nDoc = parse(createCommentElement(commentArray.data[i]));
                post.getElementsByClassName('placeForComment').item(0).append(nDoc.body);
            }
            place.style.display = 'unset';
            form.style.display = 'unset';
        } else {
            place.style.display = 'none';
            form.style.display = 'none';
        }
    });
    post.getElementsByClassName('commentArea').item(0).addEventListener('submit', e =>
        sendComment(post, e));
}


async function sendComment(post, e){
    e.preventDefault();
    const form = e.target;
    const data = new FormData(form);
    const update = updateOptions({});
    update.headers['Content-Type'] = 'application/x-www-form-urlencoded';
    axios.post(
        'http://127.0.0.1:8089/publications/' + post.id + '/comment', data, update)
        .then(function (response) {
            let comment = {
                text : data.get('comment'),
                time : Date.now(),
                postID : data.get('postId'),
                userID : data.get('userId')
            }
            console.log(comment);
            post.getElementsByClassName('placeForComment')[0].append(parse(createCommentElement(comment)).body);
            console.log(response);
        })
        .catch(function (error) {
            console.log(error);
        });
}

document.getElementById('addBtn').addEventListener('click', function (){
    const form = document.getElementById('updatePhoto');
    if (form.style.display === 'none') {
        form.style.display = 'unset';
    } else {
        form.style.display = 'none';
    }
})

const posts = document.getElementById('posts');
document.getElementById('showBtn').addEventListener('click', function (e) {
    e.preventDefault();
    if(posts.style.display === 'none'){
        axios.get(
            'http://127.0.0.1:8089/publications'
            , updateOptions({})).then(function (response) {
            const collection = response.data;

            while (posts.firstChild) {
                posts.removeChild(posts.firstChild);
            }
            posts.style.display = 'flex';
            for (let i = 0; i < collection.length; i++) {
                axios.get('http://127.0.0.1:8089/publications/image/' + collection[i].imageID,
                    updateOptions({}))
                    .then(function (image) {
                        const post = {
                            id: collection[i].id,
                            image: image.data,
                            description: collection[i].description,
                            user: collection[i].userID,
                            time: collection[i].time
                        }
                        const postElement = createPostElement(post);
                        const nDoc = parse(postElement);
                        const card = nDoc.body.getElementsByClassName('card').item(0);
                        axios.get('http://127.0.0.1:8089/publications/' + card.id + '/like',
                            updateOptions({}))
                            .then(function (heartResponse) {
                                addPost(postElement);
                                if (heartResponse.data !== null && heartResponse.data !== '') {
                                    document.getElementById(post.id).getElementsByClassName('bi bi-heart')[0]
                                        .className = 'bi bi-heart-fill like_icon heart text-danger';
                                }
                                console.log(heartResponse);
                                axios.get('http://127.0.0.1:8089/publications/' + card.id + '/bookmark',
                                    updateOptions({}))
                                    .then(function (bookmarkResponse) {
                                        if (bookmarkResponse.data !== null && bookmarkResponse.data !== '') {
                                            document.getElementById(post.id)
                                                .getElementsByClassName('bi bi-bookmark')[0]
                                                .className = 'bi bi-bookmark-fill';
                                        }
                                        console.log(bookmarkResponse);
                                    }).catch(function (error) {
                                    console.log(error);
                                })
                            }).catch(function (error) {
                            console.log(error)
                        })
                    }).catch(function (error){
                    console.log(error);
                });
            }
            initUserIdField();
        })
            .catch(function (error) {
                console.log(error);
            });
    } else {
        posts.style.display = 'none';
    }
});

const auth = document.getElementById('auth');
auth.addEventListener('click', authSplashScreen);
auth.click();
function authSplashScreen(e){
    e.preventDefault()
    loginPage.style.display = 'unset';
    loginPage.className = 'splash';
}

$('.message a').click(function(){
    $('form').animate({height: "toggle", opacity: "toggle"}, "slow");
});

function parseJson(data){
    let object = {};
    data.forEach(function(value, key){
        object[key] = value;
    });
    return JSON.stringify(object);
}


const registerForm = document.getElementById('register-form');
registerForm.addEventListener('submit', registerHandler);
async function registerHandler(e){
    e.preventDefault();
    const form = e.target;
    const data = new FormData(form);
    const json = parseJson(data);
    const user = await axios.post('http://127.0.0.1:8089/users/register', json,
        {
            headers: {
                'Content-Type':'application/json'
            }
        });
    document.getElementById('signIn').click();
    console.log(user);
}

const postArray = document.getElementsByClassName('card');
async function initUserIdField() {
    const account = await axios.post('http://127.0.0.1:8089/users/auth', '', updateOptions({}));
    for (let i = 0; i < postArray.length; i++) {
        postArray[i].getElementsByClassName('commentUserId').item(0).value = account.data.id;
    }
}

loginForm.addEventListener('submit', loginHandler);
async function loginHandler(e){
    e.preventDefault();
    const form = e.target;
    const data = new FormData(form);
    const user = Object.fromEntries(data);
    saveUser(user);
    console.log(updateOptions({}));
    axios.post('http://127.0.0.1:8089/users/auth', user, updateOptions({}))
        .then(function (response){
            userProfile.innerText = response.data.name;
            hideLoginPage();
            initUserIdField();
        }).catch(function (error) {
            console.log(error);
            localStorage.clear();
        });
}

function hideLoginPage(){
    loginPage.style.display = 'unset';
    loginPage.className = '';
    auth.style.display = 'none';
    loginPage.style.display = 'none';
}

function saveUser(user) {
    const userAsJSON = JSON.stringify(user)
    localStorage.setItem('user', userAsJSON);
}

function restoreUser() {
    const userAsJSON = localStorage.getItem('user');
    return JSON.parse(userAsJSON);
}

function updateOptions(options) {
    const update = { ...options };
    update.mode = 'cors';
    update.headers = { ... options.headers };
    update.headers['Content-Type'] = 'application/json';
    const user = restoreUser();
    if(user) {
        update.headers['Authorization'] = 'Basic ' + btoa(user.email + ':' + user.password);
    }
    return update;
}

const user = restoreUser();
userCheckHandler(user);
async function userCheckHandler(user) {
    if(user !== null){
        console.log('user exists' + user);
        const account = await axios.post('http://127.0.0.1:8089/users/auth', '', updateOptions({}));
        console.log(account);
        userProfile.innerText = account.data.name;
        hideLoginPage();
    } else {
        console.log('user doesn\'t exist');
    }
}

const logOut = document.getElementById('logout');
logOut.addEventListener('click', logOutHandler)
function logOutHandler(e){
    e.preventDefault();
    localStorage.clear();
    auth.click();
    posts.style.display = 'none';
}

document.getElementById('search-form').addEventListener('submit',
    function (e){e.preventDefault()});

