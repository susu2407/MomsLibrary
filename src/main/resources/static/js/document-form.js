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

// 새 태그 입력
function addTagToDocument(event) {
    event.preventDefault();

    const tagInput = document.getElementById("tagSearch");

    const tagName = tagInput.value.trim();

    if (tagName === "") { return; }

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

    // 입력창 초기화
    tagInput.value = "";
}