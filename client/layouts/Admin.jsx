import Cookies from "js-cookie";
import { useRouter } from "next/router";
import React, { useEffect } from "react";

import SidebarAdmin from "../components/Sidebar/SidebarAdmin";
import Header from "../containers/Header/Header";

export const AdminLayout = ({ children }) => {
  const router = useRouter();
  const token = Cookies.get("jwt");
  useEffect(() => {
    // if (token === undefined) {
    //   return router.replace("/login");
    // }
  }, []);
  return (
    <>
      {/* {token !== undefined && ( */}
        <div className="relative md:ml-72 bg-gray-100 h-full min-h-screen">
          <SidebarAdmin />
          <Header />
          <div className="w-full">{children}</div>
        </div>
      {/* )} */}
    </>
  );
};
