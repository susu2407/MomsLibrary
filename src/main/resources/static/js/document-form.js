// 태그 선택
function pickTag(element) {

    const tagName = element.innerText;
    const tagId = element.dataset.id;

    const selectedTags = document.getElementById("selectedTags");

    // 중복 방지
    const exists = selectedTags.querySelector(
        `input[value="${tagId}"]`
    );

    if (exists) return;

    // 추천 목록에서 제거
    element.remove();

    // badge 생성
    const span = document.createElement("span");
    span.className = "badge badge-blue";
    span.style.cursor = "pointer";
    span.innerText = tagName + " x";

    // 제거 이벤트
    span.onclick = function() {
        removeSelectedTag(span);
    }

    // hidden input 추가
    const input = document.createElement("input");
    input.type = "hidden";
    input.name = "tagIds";
    input.value = tagId;

    span.appendChild(input);
    selectedTags.appendChild(span);
}

// 태그 제거
function removeSelectedTag (element) {
    element.remove();
}

// 태그 검색 필터링
document.getElementById("tagSearch").addEventListener("input", function() {
    const keyword = this.value.trim().toLowerCase();
    const suggestions = document.querySelectorAll("#tagSuggest .badge");

    suggestions.forEach(badge => {
        const tagName = badge.childNodes[0].textContent.trim().toLowerCase();
        badge.style.display = tagName.includes(keyword) ? "" : "none";
    });
});


// 새 태그 입력 (기존 로직 수정 - 기존 태그인지 먼저 확인)
function addTagToDocument(event) {
    event.preventDefault();

    const tagInput = document.getElementById("tagSearch");
    const tagName = tagInput.value.trim();

    if (tagName === "") { return; }

    // 기존 목록(tagSuggest) 안에 같은 이름이 있는지 확인
    const suggestions = document.querySelectorAll("#tagSuggest .badge");
    let matched = null;

    suggestions.forEach(badge => {
        const badgeName = badge.childNodes[0].textContent.trim();
        if (badgeName === tagName) {
            matched = badge;
        }
    });

    if (matched) {
        // 기본 태그면 pickTag 로직 그대로 재사용
        pickTag(matched);
    } else {
        // 진짜 신규 태그일 때만 새로 생성
        const selectedTags = document.getElementById("selectedTags");

        // badge 생성
        const span = document.createElement("span");
        span.className = "badge badge-blue";
        span.style.cursor = "pointer";
        span.innerText = tagName + " x";

        // 제거 이벤트
        span.onclick = function() {
            removeSelectedTag(span);
        }

        // hidden input 생성
        const hidden = document.createElement("input");
        hidden.type = "hidden";
        hidden.name = "newTags";
        hidden.value = tagName;

        span.appendChild(hidden);
        selectedTags.appendChild(span);
    }
    // 입력창 초기화
    tagInput.value = "";

    // 입력창이 비었으니 숨겨졌던 추천 태그들 다시 보이게 초기화
    resetTagSuggestVisibility();
}

function resetTagSuggestVisibility() {
    document.querySelectorAll("#tagSuggest .badge").forEach(bade => {
        badge.style.display = "";
    });
}