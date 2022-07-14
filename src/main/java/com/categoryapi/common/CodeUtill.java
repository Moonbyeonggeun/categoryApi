package com.categoryapi.common;

public class CodeUtill {

    // 정상 처리 완료
    public static final String RESULT_SUCCESS = "200";

    // 등록, 수정 실패 (데이터 중복)
    public static final String RESULT_OVERLAP = "300";

    // 처리 실패
    public static final String RESULT_ERROR = "400";

    // 등록, 수정 실패 (잘못된 등록방법)
    public static final String RESULT_FAIL = "500";

    // 검색 결과 없음
    public static final String RESULT_EMPTY = "600";

    // 삭제 플래그 0:사용가능
    public static final int DELETE_FLAG_ON = 0;

    // 삭제 플래그 1:사용불가
    public static final int DELETE_FLAG_OFF = 1;
}
