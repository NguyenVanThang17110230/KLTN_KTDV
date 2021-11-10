import React, { useState } from "react";
import Image from "next/image";
import { AdminLayout } from "../../layouts/Admin";

const Profile = () => {
  const [isProfile, setIsProfile] = useState(true);

  return (
    <div className="flex justify-center">
      <div className="w-3/4 py-10">
        <div className="bg-white w-full flex rounded-md shadow-sm">
          <div className="border-r-2 p-4 w-1/4">
            <div className="text-center">
              <Image
                alt="..."
                className="w-full align-middle rounded-full"
                src="/static/img/avt.jpg"
                width={150}
                height={150}
              />
            </div>
            <div className="text-center mt-3 font-bold text-gray-600 text-lg">
              Name
            </div>
            <div className="text-center">Admin@gmail.com</div>
          </div>
          <div className="p-4 w-3/4">
            <div className="border-b border-red-500">
              <div className="font-bold text-2xl text-gray-700">
                Profile and Password
              </div>
              <div className="flex mt-4">
                <div
                  className={
                    "py-3 px-4 cursor-pointer font-medium flex " +
                    (isProfile ? "border-b-2 border-red-500" : null)
                  }
                  onClick={() => setIsProfile(true)}
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    className="icon icon-tabler icon-tabler-id mr-2"
                    width={24}
                    height={24}
                    viewBox="0 0 24 24"
                    strokeWidth="1.5"
                    stroke="#ff4500"
                    fill="none"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                  >
                    <path stroke="none" d="M0 0h24v24H0z" fill="none" />
                    <rect x={3} y={4} width={18} height={16} rx={3} />
                    <circle cx={9} cy={10} r={2} />
                    <line x1={15} y1={8} x2={17} y2={8} />
                    <line x1={15} y1={12} x2={17} y2={12} />
                    <line x1={7} y1={16} x2={17} y2={16} />
                  </svg>
                  Profile
                </div>
                <div
                  className={
                    "cursor-pointer py-3 px-4 font-medium flex " +
                    (!isProfile ? "border-b-2 border-red-500" : null)
                  }
                  onClick={() => setIsProfile(false)}
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    className="icon icon-tabler icon-tabler-shield-lock mr-2"
                    width={24}
                    height={24}
                    viewBox="0 0 24 24"
                    strokeWidth="1.7"
                    stroke="#ff4500"
                    fill="none"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                  >
                    <path stroke="none" d="M0 0h24v24H0z" fill="none" />
                    <path d="M12 3a12 12 0 0 0 8.5 3a12 12 0 0 1 -8.5 15a12 12 0 0 1 -8.5 -15a12 12 0 0 0 8.5 -3" />
                    <circle cx={12} cy={11} r={1} />
                    <line x1={12} y1={12} x2={12} y2="14.5" />
                  </svg>
                  Password
                </div>
              </div>
            </div>
            {isProfile ? <div>profile</div> : <div>password</div>}
          </div>
        </div>
      </div>
    </div>
  );
};

Profile.getLayout = function getLayout(page) {
  return <AdminLayout>{page}</AdminLayout>;
};

export default Profile;
