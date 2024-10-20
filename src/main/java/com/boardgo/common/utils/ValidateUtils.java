package com.boardgo.common.utils;

import static com.boardgo.common.exception.advice.dto.ErrorCode.BAD_REQUEST;

import com.boardgo.common.exception.CustomIllegalArgumentException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ValidateUtils {

    /** 한글,영문,숫자 만 포함하는지 확인 */
    public static boolean containsHanEngNum(String data) {
        return Pattern.matches("^[0-9a-zA-Zㄱ-ㅎ가-힣]*$", data);
    }

    /** 한글,영문,숫자, 스페이스 만 포함하는지 확인 */
    public static boolean containsHanEngNumSpace(String data) {
        return Pattern.matches("^[0-9a-zA-Zㄱ-ㅎ가-힣\\s]*$", data);
    }

    /** 닉네임 유효성 검사 */
    public static boolean validateNickname(String nickname) {
        if (nickname.length() < 2 || nickname.length() > 8) {
            throw new CustomIllegalArgumentException(BAD_REQUEST.getCode(), "글자 수는 2~8자 까지 가능합니다.");
        }
        if (!containsHanEngNum(nickname)) {
            throw new CustomIllegalArgumentException(BAD_REQUEST.getCode(), "한글,영문,숫자만 입력 가능합니다.");
        }
        return true;
    }

    /** PR태그 유효성 검사 */
    public static boolean validatePrTag(List<String> prTags) {
        if (prTags.size() > 10) {
            throw new CustomIllegalArgumentException(
                    BAD_REQUEST.getCode(), "PR 태그 수는 10개 까지 가능합니다.");
        }

        for (String tag : prTags) {
            tag = tag.trim();
            if (tag.length() > 10) {
                throw new CustomIllegalArgumentException(
                        BAD_REQUEST.getCode(), "PR태그 글자 수는 10자 까지 가능합니다.");
            }
            if (!containsHanEngNumSpace(tag)) {
                throw new CustomIllegalArgumentException(
                        BAD_REQUEST.getCode(), "한글,영문,숫자,스페이스만 입력 가능합니다.");
            }
        }
        return true;
    }

    public static boolean validatePassword(String password) {
        Pattern regex = Pattern.compile("\\s+");
        Matcher matcher = regex.matcher(password);

        if (matcher.find()) {
            throw new CustomIllegalArgumentException(BAD_REQUEST.getCode(), "문자열에 공백이 입력되었습니다.");
        }
        if (password.length() < 8 || password.length() > 50) {
            throw new CustomIllegalArgumentException(
                    BAD_REQUEST.getCode(), "비밀번호는 8~50자 까지 가능합니다.");
        }
        return true;
    }
}
