package com.studyMate.studyMate.domain.user.service;

import com.studyMate.studyMate.global.error.CustomException;
import com.studyMate.studyMate.global.error.ErrorCode;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);

            log.info("이메일 전송 성공: {}", to);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAIL_SEND_EMAIL);
        }
    }

    public void sendLocalSignUpEmail(String to, String subject, String verificationCode) {
        String content = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Study-Mate 회원가입 인증</title>
            </head>
            <body style="margin: 0; padding: 20px; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Arial, sans-serif; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh;">
                <table width="100%" cellpadding="0" cellspacing="0" style="max-width: 600px; margin: 0 auto;">
                    <tr>
                        <td>
                            <!-- 메인 컨테이너 -->
                            <div style="background: #ffffff; border-radius: 20px; box-shadow: 0 20px 40px rgba(0,0,0,0.1); overflow: hidden;">
                                
                                <!-- 헤더 -->
                                <div style="background: linear-gradient(135deg, #4f46e5 0%, #7c3aed 100%); padding: 40px 30px; text-align: center; color: white;">
                                    <div style="font-size: 32px; font-weight: 700; margin-bottom: 10px; letter-spacing: -0.5px;">
                                        📚 Study-Mate
                                    </div>
                                    <div style="font-size: 16px; opacity: 0.9; font-weight: 300;">
                                        함께 성장하는 스터디 플랫폼
                                    </div>
                                </div>
                                
                                <!-- 콘텐츠 -->
                                <div style="padding: 50px 40px; text-align: center;">
                                    <h1 style="font-size: 24px; color: #1f2937; margin-bottom: 20px; font-weight: 500; margin-top: 0;">
                                        회원가입을 환영합니다! 🎉
                                    </h1>
                                    
                                    <p style="font-size: 16px; color: #6b7280; line-height: 1.6; margin-bottom: 40px;">
                                        Study-Mate에 가입해 주셔서 진심으로 감사합니다.<br>
                                        아래 인증코드를 입력하여 회원가입을 완료해 주세요.
                                    </p>
                                    
                                    <!-- 인증코드 박스 -->
                                    <div style="background: linear-gradient(135deg, #f3f4f6 0%, #e5e7eb 100%); border: 2px dashed #d1d5db; border-radius: 15px; padding: 30px; margin: 30px 0;">
                                        <div style="font-size: 14px; color: #6b7280; margin-bottom: 10px; text-transform: uppercase; letter-spacing: 1px; font-weight: 500;">
                                            인증코드
                                        </div>
                                        <div style="font-size: 36px; font-weight: 700; color: #4f46e5; letter-spacing: 8px; font-family: 'Courier New', monospace; margin: 15px 0;">
                                            ${verificationCode}
                                        </div>
                                    </div>
                                    
                                    <!-- 경고 박스 -->
                                    <div style="background: #fef3cd; border: 1px solid #fbbf24; border-radius: 10px; padding: 20px; margin: 30px 0;">
                                        <div style="font-size: 16px; font-weight: 600; color: #92400e; margin-bottom: 8px;">
                                            ⚠️ 중요 안내
                                        </div>
                                        <div style="font-size: 14px; color: #92400e; line-height: 1.5;">
                                            • 이 인증코드는 <strong>5분간</strong> 유효합니다.<br>
                                            • 본인이 요청하지 않은 경우, 이 이메일을 무시해 주세요.<br>
                                            • 인증코드를 타인과 공유하지 마세요.
                                        </div>
                                    </div>
                                    
                                    <!-- 구분선 -->
                                    <div style="height: 2px; background: linear-gradient(135deg, #4f46e5 0%, #7c3aed 100%); margin: 30px 0; border-radius: 1px;"></div>
                                    
                                    <p style="font-size: 16px; color: #6b7280; line-height: 1.6; margin-bottom: 40px;">
                                        Study-Mate와 함께 효율적인 학습을 시작해보세요!<br>
                                    </p>
                                </div>
                                
                                <!-- 푸터 -->
                                <div style="background: #f9fafb; padding: 30px; text-align: center; border-top: 1px solid #e5e7eb;">
                                    <p style="font-size: 14px; color: #9ca3af; line-height: 1.5; margin: 0;">
                                        본 메일은 Study-Mate 회원가입 인증을 위해 발송되었습니다.<br>
                                        Study-Mate | 서울특별시 | nightshiftteam2024@gmail.com
                                    </p>
                                    <div style="margin-top: 20px; font-size: 16px; color: #4f46e5; font-weight: 500;">
                                        💫 Night Shift Team
                                    </div>
                                </div>
                                
                            </div>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """;

        String formattedContent = content.replace("${verificationCode}", verificationCode);
        sendHtmlEmail(to, subject, formattedContent);
    }

}