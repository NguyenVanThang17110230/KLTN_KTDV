import React from 'react'
import { AdminLayout } from '../../layouts/Admin'

const Profile = () => {
    return (
        <div>
            helll
        </div>
    )
}

Profile.getLayout = function getLayout(page) {
    return <AdminLayout>{page}</AdminLayout>;
}

export default Profile

