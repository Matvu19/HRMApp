# HRMApp ERD

```mermaid
erDiagram
    ORG_UNIT ||--o{ EMPLOYEE_PROFILE : contains
    POSITION ||--o{ EMPLOYEE_PROFILE : assigns
    EMPLOYEE_PROFILE ||--o{ USER_ACCOUNT : owns
    USER_ACCOUNT ||--o{ DEVICE_SESSION : opens
    ROLE ||--o{ PERMISSION_POLICY : grants
    USER_ACCOUNT ||--o{ USER_ROLE : maps
    ROLE ||--o{ USER_ROLE : maps
    EMPLOYEE_PROFILE ||--o{ EMPLOYMENT_CONTRACT : signs
    SHIFT ||--o{ SHIFT_ASSIGNMENT : schedules
    EMPLOYEE_PROFILE ||--o{ SHIFT_ASSIGNMENT : receives
    SHIFT_ASSIGNMENT ||--o{ ATTENDANCE_EVENT : records
    EMPLOYEE_PROFILE ||--o{ ATTENDANCE_DAILY : aggregates
    LEAVE_TYPE ||--o{ LEAVE_REQUEST : classifies
    EMPLOYEE_PROFILE ||--o{ LEAVE_REQUEST : submits
    APPROVAL_FLOW ||--o{ LEAVE_REQUEST : governs
    APPROVAL_FLOW ||--o{ OVERTIME_REQUEST : governs
    APPROVAL_FLOW ||--o{ APPROVAL_STEP : contains
    EMPLOYEE_PROFILE ||--o{ APPROVAL_STEP : acts
    PAYROLL_RUN ||--o{ PAYSLIP : publishes
    EMPLOYEE_PROFILE ||--o{ PAYSLIP : receives
    USER_ACCOUNT ||--o{ NOTIFICATION_EVENT : receives
    EMPLOYEE_PROFILE ||--o{ TICKET_REQUEST : creates
    EMPLOYEE_PROFILE ||--o{ ASSET_ASSIGNMENT : receives
    USER_ACCOUNT ||--o{ AUDIT_LOG : acts


---

## C. Kiểm tra thành công

### Cách kiểm tra file đã tạo đúng
Trong Terminal:

```bash
cd ~/HRMApp
find database -maxdepth 2 -type f
find docs -maxdepth 1 -type f

database/migrations/V1__init_schema.sql
database/seed/V2__seed_demo.sql
docs/erd.md