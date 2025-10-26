package electronic_health_record.Departments;

import java.util.List;
import java.util.Optional;

public interface DepartmentDao {
    /**
     * Thêm một khoa mới vào cơ sở dữ liệu.
     */
    void addDepartment(Department department);

    /**
     * Lấy thông tin một khoa bằng ID.
     */
    Optional<Department> getDepartmentById(int departmentId);

    /**
     * Lấy danh sách tất cả các khoa.
     */
    List<Department> getAllDepartments();

    /**
     * Lấy danh sách các khoa thuộc một bệnh viện cụ thể.
     */
    List<Department> getDepartmentsByHospital(int hospitalId);

    /**
     * Cập nhật thông tin của một khoa.
     */
    void updateDepartment(Department department);

    /**
     * Xóa một khoa khỏi cơ sở dữ liệu bằng ID.
     */
    void deleteDepartment(int departmentId);
}