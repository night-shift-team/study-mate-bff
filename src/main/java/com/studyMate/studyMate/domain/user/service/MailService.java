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

    public void sendResetPasswordVerificationEmail(String to, String subject, String resetCode) {
        String content = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Study-Mate 비밀번호 재설정</title>
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
                                        비밀번호 재설정 요청 🔐
                                    </h1>
                                    
                                    <p style="font-size: 16px; color: #6b7280; line-height: 1.6; margin-bottom: 40px;">
                                        비밀번호 재설정 요청을 받았습니다.<br>
                                        아래 인증코드를 입력하여 새로운 비밀번호를 설정해 주세요.
                                    </p>
                                    
                                    <!-- 인증코드 박스 -->
                                    <div style="background: linear-gradient(135deg, #fef3cd 0%, #fde68a 100%); border: 2px dashed #f59e0b; border-radius: 15px; padding: 30px; margin: 30px 0;">
                                        <div style="font-size: 14px; color: #92400e; margin-bottom: 10px; text-transform: uppercase; letter-spacing: 1px; font-weight: 500;">
                                            재설정 인증코드
                                        </div>
                                        <div style="font-size: 36px; font-weight: 700; color: #d97706; letter-spacing: 8px; font-family: 'Courier New', monospace; margin: 15px 0;">
                                            ${resetCode}
                                        </div>
                                    </div>
                                    
                                    <!-- 경고 박스 -->
                                    <div style="background: #fee2e2; border: 1px solid #f87171; border-radius: 10px; padding: 20px; margin: 30px 0;">
                                        <div style="font-size: 16px; font-weight: 600; color: #dc2626; margin-bottom: 8px;">
                                            🚨 보안 안내
                                        </div>
                                        <div style="font-size: 14px; color: #dc2626; line-height: 1.5;">
                                            • 이 인증코드는 <strong>10분간</strong> 유효합니다.<br>
                                            • 본인이 요청하지 않은 경우, 즉시 비밀번호를 변경해 주세요.<br>
                                            • 인증코드를 타인과 절대 공유하지 마세요.
                                        </div>
                                    </div>
                                    
                                    <!-- 추가 안내 -->
                                    <div style="background: #eff6ff; border: 1px solid #60a5fa; border-radius: 10px; padding: 20px; margin: 30px 0;">
                                        <div style="font-size: 16px; font-weight: 600; color: #1d4ed8; margin-bottom: 8px;">
                                            💡 도움말
                                        </div>
                                        <div style="font-size: 14px; color: #1d4ed8; line-height: 1.5;">
                                            • 비밀번호는 8자 이상, 영문/숫자/특수문자 조합으로 설정해 주세요.<br>
                                            • 다른 사이트와 다른 고유한 비밀번호를 사용해 주세요.
                                        </div>
                                    </div>
                                    
                                    <!-- 구분선 -->
                                    <div style="height: 2px; background: linear-gradient(135deg, #4f46e5 0%, #7c3aed 100%); margin: 30px 0; border-radius: 1px;"></div>
                                    
                                    <p style="font-size: 16px; color: #6b7280; line-height: 1.6; margin-bottom: 40px;">
                                        계정 보안에 문제가 있으시면 즉시 고객센터로 연락해 주세요.<br>
                                    </p>
                                </div>
                                
                                <!-- 푸터 -->
                                <div style="background: #f9fafb; padding: 30px; text-align: center; border-top: 1px solid #e5e7eb;">
                                    <p style="font-size: 14px; color: #9ca3af; line-height: 1.5; margin: 0;">
                                        본 메일은 Study-Mate 비밀번호 재설정을 위해 발송되었습니다.<br>
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

        String formattedContent = content.replace("${resetCode}", resetCode);
        sendHtmlEmail(to, subject, formattedContent);
    }

    public void sendResetPasswordResultEmail(String to, String subject, String newPassword) {
        String content = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Study-Mate 새 비밀번호 안내</title>
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
                                        비밀번호가 성공적으로 재설정되었습니다! ✅
                                    </h1>
                                    
                                    <p style="font-size: 16px; color: #6b7280; line-height: 1.6; margin-bottom: 40px;">
                                        요청하신 비밀번호 재설정이 완료되었습니다.<br>
                                        새로운 임시 비밀번호로 로그인 후 반드시 비밀번호를 변경해 주세요.
                                    </p>
                                    
                                    <!-- 새 비밀번호 박스 -->
                                    <div style="background: linear-gradient(135deg, #dcfce7 0%, #bbf7d0 100%); border: 2px dashed #16a34a; border-radius: 15px; padding: 30px; margin: 30px 0;">
                                        <div style="font-size: 14px; color: #15803d; margin-bottom: 10px; text-transform: uppercase; letter-spacing: 1px; font-weight: 500;">
                                            새 임시 비밀번호
                                        </div>
                                        <div style="font-size: 36px; font-weight: 700; color: #15803d; letter-spacing: 4px; font-family: 'Courier New', monospace; margin: 15px 0;">
                                            ${newPassword}
                                        </div>
                                    </div>
                                    
                                    <!-- 중요 안내 박스 -->
                                    <div style="background: #fef3cd; border: 1px solid #f59e0b; border-radius: 10px; padding: 20px; margin: 30px 0;">
                                        <div style="font-size: 16px; font-weight: 600; color: #92400e; margin-bottom: 8px;">
                                            🔒 중요 안내
                                        </div>
                                        <div style="font-size: 14px; color: #92400e; line-height: 1.5;">
                                            • 이 비밀번호는 <strong>임시 비밀번호</strong>입니다.<br>
                                            • 로그인 후 즉시 새로운 비밀번호로 변경해 주세요.<br>
                                            • 이 이메일을 다른 사람과 공유하지 마세요.
                                        </div>
                                    </div>
                                    
                                    <!-- 보안 가이드 -->
                                    <div style="background: #eff6ff; border: 1px solid #60a5fa; border-radius: 10px; padding: 20px; margin: 30px 0;">
                                        <div style="font-size: 16px; font-weight: 600; color: #1d4ed8; margin-bottom: 8px;">
                                            🛡️ 보안 권장사항
                                        </div>
                                        <div style="font-size: 14px; color: #1d4ed8; line-height: 1.5;">
                                            • 새 비밀번호는 8자 이상으로 설정해 주세요.<br>
                                            • 영문, 숫자, 특수문자를 조합해 주세요.<br>
                                            • 다른 사이트와 다른 고유한 비밀번호를 사용해 주세요.
                                        </div>
                                    </div>
                                    
                                    <!-- 긴급 연락 -->
                                    <div style="background: #fee2e2; border: 1px solid #f87171; border-radius: 10px; padding: 20px; margin: 30px 0;">
                                        <div style="font-size: 16px; font-weight: 600; color: #dc2626; margin-bottom: 8px;">
                                            🚨 본인이 요청하지 않았다면?
                                        </div>
                                        <div style="font-size: 14px; color: #dc2626; line-height: 1.5;">
                                            즉시 고객센터(nightshiftteam2024@gmail.com)로 연락하여<br>
                                            계정 보안 점검을 받으시기 바랍니다.
                                        </div>
                                    </div>
                                    
                                    <!-- 구분선 -->
                                    <div style="height: 2px; background: linear-gradient(135deg, #4f46e5 0%, #7c3aed 100%); margin: 30px 0; border-radius: 1px;"></div>
                                    
                                    <p style="font-size: 16px; color: #6b7280; line-height: 1.6; margin-bottom: 40px;">
                                        Study-Mate와 함께 안전하고 즐거운 학습을 계속해보세요!<br>
                                    </p>
                                </div>
                                
                                <!-- 푸터 -->
                                <div style="background: #f9fafb; padding: 30px; text-align: center; border-top: 1px solid #e5e7eb;">
                                    <p style="font-size: 14px; color: #9ca3af; line-height: 1.5; margin: 0;">
                                        본 메일은 Study-Mate 비밀번호 재설정 완료를 위해 발송되었습니다.<br>
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

        String formattedContent = content.replace("${newPassword}", newPassword);
        sendHtmlEmail(to, subject, formattedContent);
    }


}