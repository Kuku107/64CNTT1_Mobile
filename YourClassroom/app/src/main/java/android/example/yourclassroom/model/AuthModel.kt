package android.example.yourclassroom.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Patterns
import com.google.firebase.database.FirebaseDatabase

class AuthModel {
    private val auth = FirebaseAuth.getInstance()
    private val databaseUrl = "https://yourclassroom-6d328-default-rtdb.asia-southeast1.firebasedatabase.app/"
    private val database = FirebaseDatabase.getInstance(databaseUrl).reference

    suspend fun login(user: User): Result<FirebaseUser?> = withContext(Dispatchers.IO) {
        return@withContext try {
            val result = auth.signInWithEmailAndPassword(user.email, user.password!!).await()
            if (result.user?.isEmailVerified == true) {
                Result.success(result.user)
            } else {
                Result.failure(Exception("Vui lòng xác nhận email của bạn!"))
            }
        } catch (e: FirebaseAuthException) {
            Result.failure(Exception("Thông tin đăng nhập không chính xác!"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(user: User): Result<FirebaseUser> = withContext(Dispatchers.IO) {
        return@withContext try {
            if (!Patterns.EMAIL_ADDRESS.matcher(user.email).matches()) {
                return@withContext Result.failure(Exception("Email không hợp lệ!"))
            }
            if (user.password == null || user.password.length < 6) {
                return@withContext Result.failure(Exception("Mật khẩu phải có ít nhất 6 ký tự!"))
            }

            val result = auth.createUserWithEmailAndPassword(user.email, user.password).await()
            val firebaseUser = result.user ?: throw Exception("Không thể tạo người dùng")

            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(user.fullName)
                .build()
            firebaseUser.updateProfile(profileUpdates).await()

            firebaseUser.sendEmailVerification().await()
            Result.success(firebaseUser)

        } catch (e: FirebaseAuthUserCollisionException) {
            Result.failure(Exception("Email đã được sử dụng!"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    internal suspend fun saveUserToDatabase(firebaseUser: FirebaseUser, user: User) = withContext(Dispatchers.IO) {
        val userEmail = firebaseUser.email
        val userDisplayName = firebaseUser.displayName
        val userId = firebaseUser.uid

        val userRef = database.child("users").child(userId)
        val userMap = mapOf(
            "email" to userEmail,
            "fullname" to userDisplayName,
            "id" to userId
        )

        try {
            userRef.setValue(userMap).await()
            Result.success(Unit)
        } catch (e: Exception) {
            throw Exception("Lỗi khi lưu người dùng vào cơ sở dữ liệu: ${e.message}")
        }
    }

    suspend fun forgotPassword(email: String): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return@withContext Result.failure(Exception("Email không hợp lệ!"))
            }
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: FirebaseAuthException) {
            val errorCode = e.errorCode
            if (errorCode == "ERROR_USER_NOT_FOUND") {
                Result.failure(Exception("Email không tồn tại trong hệ thống!"))
            } else {
                Result.failure(Exception("Lỗi: ${e.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }

}