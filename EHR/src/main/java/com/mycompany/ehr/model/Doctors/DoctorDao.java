package electronic_health_record.Doctors;

import java.util.List;
import java.util.Optional;

public interface DoctorDao {

    /**
     * Thêm một bác sĩ mới vào CSDL.
     * @param doctor Đối tượng Doctor cần thêm.
     */
    void addDoctor(Doctor doctor);

    /**
     * Lấy thông tin bác sĩ bằng ID.
     * @param doctorId ID của bác sĩ.
     * @return Optional chứa Doctor nếu tìm thấy.
     */
    Optional<Doctor> getDoctorById(int doctorId);

    /**
     * Lấy danh sách tất cả bác sĩ.
     * @return List các đối tượng Doctor.
     */
    List<Doctor> getAllDoctors();

    /**
     * Lấy danh sách bác sĩ theo khoa.
     * @param departmentId ID của khoa.
     * @return List các bác sĩ thuộc khoa đó.
     */
    List<Doctor> getDoctorsByDepartment(int departmentId);

    /**
     * Cập nhật thông tin của một bác sĩ.
     * @param doctor Đối tượng Doctor với thông tin đã cập nhật.
     */
    void updateDoctor(Doctor doctor);

    /**
     * Xóa một bác sĩ khỏi CSDL.
     * @param doctorId ID của bác sĩ cần xóa.
     */
    void deleteDoctor(int doctorId);
}