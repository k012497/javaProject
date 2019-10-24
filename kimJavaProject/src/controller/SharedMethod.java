package controller;

import java.text.DecimalFormat;
import java.text.ParsePosition;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class SharedMethod {

	// 경고창 생성
	public static void alertDisplay(int type, String title, String headerText, String contentText) {
		Alert alert = null;
		switch (type) {
		case 1:
			alert = new Alert(AlertType.WARNING);
			break;
		case 2:
			alert = new Alert(AlertType.CONFIRMATION);
			break;
		case 3:
			alert = new Alert(AlertType.ERROR);
			break;
		case 4:
			alert = new Alert(AlertType.NONE);
			break;
		case 5:
			alert = new Alert(AlertType.INFORMATION);
			break;
		}
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(headerText + "\n" + contentText);
		alert.setResizable(false);
		alert.showAndWait();
	}// end of alertDisplay

	// 텍스트필드 입력값 포맷설정기능(11자리 숫자만 받음)
	public static void inputDecimalFormatElevenDigit(TextField textField) {
		// 숫자만 입력(정수만 입력받음), 실수입력받고싶으면new DecimalFormat("###.#");
		DecimalFormat format = new DecimalFormat("##############");
		// 점수 입력시 길이 제한 이벤트 처리
		textField.setTextFormatter(new TextFormatter<>(event -> {
			// 입력받은 내용이 없으면 이벤트를 리턴함.
			if (event.getControlNewText().isEmpty()) {
				return event;
			}
			// 구문을 분석할 시작 위치를 지정함. 세자리까지 계속해서 점검하겠다.
			ParsePosition parsePosition = new ParsePosition(0);
			// 입력받은 내용과 분석위치 를 지정한지점부터 format 내용과 일치한지 분석함.
			Object object = format.parse(event.getControlNewText(), parsePosition);
			// 리턴값이 null 이거나,입력한길이와 구문분석위치값이 적어버리면(다 분석하지못했음을 뜻함)거나,입력한길이가 4이면(3자리를 넘었음을
			// 뜻함.) 이면 null 리턴함.
			if (object == null || parsePosition.getIndex() < event.getControlNewText().length()
					|| event.getControlNewText().length() == 12) {
				return null;
			} else {
				return event; // 값을 돌려주겠다.
			}
		}));
	}

	// 영문 + 숫자만 입력 받는 함수
	public static boolean checkOnlyNumberAndEnglish(String textInput) {
		char chrInput;

		for (int i = 0; i < textInput.length(); i++) {

			chrInput = textInput.charAt(i); // 입력받은 텍스트에서 문자 하나하나 가져와서 체크

			if (chrInput >= 0x61 && chrInput <= 0x7A) {
				// 영문(소문자) OK!
			} else if (chrInput >= 0x41 && chrInput <= 0x5A) {
				// 영문(대문자) OK!
			} else if (chrInput >= 0x30 && chrInput <= 0x39) {
				// 숫자 OK!
			} else {
				alertDisplay(5, "[오류발생]", "영문과 숫자 이외에 다른 글자가 포함되어있습니다.", "아이디와 비밀번호는 영문과 숫자로만 입력해주세요");
				return false; // 영문자도 아니고 숫자도 아님!
			}
		}
		return true;
	}

}
